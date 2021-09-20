import groovy.transform.Field
import hubitat.helper.HexUtils
import hubitat.device.HubAction

metadata {
	definition(name: "Eurotronic Sensor", namespace: "aelfot", author: "Ravil Rubashkin") { 
		capability "CarbonDioxideMeasurement"
		capability "TemperatureMeasurement"
		capability "RelativeHumidityMeasurement"
		capability "Sensor"
		capability "Polling"
		capability "Configuration"

		attribute "VOC",					"number"
		attribute "DewPoint", 				"number"
		attribute "VOC-Niveau",				"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]
		attribute "carbonDioxide-Niveau",	"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]
		attribute "Home-Health",			"enum", ["gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]
		attribute "DeviceResetLocally",		"bool"
		attribute "groups", "number"

		/*command "testNode",		[[name: "Power Level", type: "NUMBER", description: "Power 0-9"],
													 [name: "Test Frame Count", type: "NUMBER", description: ""],
													 [name: "Node ID", type: "STRING", description: ""]]*/
		command "setAssociationGroup"
		command "setAssociationGroup", [[name: "Group Number*",type:"NUMBER", description: "Provide the association group number to edit"], 
										[name: "Z-Wave Node*", type:"STRING", description: "Enter the node number (in hex) associated with the node"],
										[name: "Action*", type:"ENUM", constraints: ["Add", "Remove"]],
										[name:"Multi-channel Endpoint", type:"NUMBER", description: "Currently not implemented"]]

		fingerprint  mfr:"0148", prod:"0005", deviceId:"0001", inClusters:"0x5E,0x6C,0x55,0x98,0x9F"		
		}	

	def tempEinheit =  [:]
		tempEinheit << [0:"Celcius"]
		tempEinheit << [1:"Fahrenheit"]

	def aufloesung =  [:]
		aufloesung << [0:"Keine Nachkommastelle"]
		aufloesung << [1:"Eine Nachkommastelle"]
		aufloesung << [2:"Zwei Nachkommastellen"]

	def vocChangeReport =  [:]
		vocChangeReport << [0:"Keine Meldung"]
		vocChangeReport << [1:"Meldung bei Delta 0.1 ppm"]
		vocChangeReport << [2:"Meldung bei Delta 0.2 ppm"]
		vocChangeReport << [3:"Meldung bei Delta 0.3 ppm"]
		vocChangeReport << [4:"Meldung bei Delta 0.4 ppm"]
		vocChangeReport << [5:"Meldung bei Delta 0.5 ppm"]
		vocChangeReport << [6:"Meldung bei Delta 0.6 ppm"]
		vocChangeReport << [7:"Meldung bei Delta 0.7 ppm"]
		vocChangeReport << [8:"Meldung bei Delta 0.8 ppm"]
		vocChangeReport << [9:"Meldung bei Delta 0.9 ppm"]
		vocChangeReport << [10:"Meldung bei Delta 1.0 ppm"]

	def co2ChangeReport =  [:]
		co2ChangeReport << [0:"Keine Meldung"]
		co2ChangeReport << [1:"Meldung bei Delta 100 ppm"]
		co2ChangeReport << [2:"Meldung bei Delta 200 ppm"]
		co2ChangeReport << [3:"Meldung bei Delta 300 ppm"]
		co2ChangeReport << [4:"Meldung bei Delta 400 ppm"]
		co2ChangeReport << [5:"Meldung bei Delta 500 ppm"]
		co2ChangeReport << [6:"Meldung bei Delta 600 ppm"]
		co2ChangeReport << [7:"Meldung bei Delta 700 ppm"]
		co2ChangeReport << [8:"Meldung bei Delta 800 ppm"]
		co2ChangeReport << [9:"Meldung bei Delta 900 ppm"]
		co2ChangeReport << [10:"Meldung bei Delta 1000 ppm"]

	preferences {
		input name: "parameter1",	type: "decimal",	title: "Temperatur on Change Reporting",	description: "0.1 - 5.0°C. Default: 0.5°C. 0 - off",	defaultValue: 0.5,	range:"0..5.0"
		input name: "parameter2",	type: "decimal",	title: "Feuchtigkeit on Change Reporting",	description: "1-10%. Default: 5%. 0 - off",				defaultValue: 5,	range:"0..10"
		input name: "parameter3",	type: "enum",		title: "Temperatureeinheit", 				options: tempEinheit,						 			description: "Default: °C",						defaultValue:0
		input name: "parameter4",	type: "enum",		title: "Auflösung Temperatur",				options: aufloesung, 									description: "Default: Eine Nachkommastelle",	defaultValue:1
		input name: "parameter5",	type: "enum", 		title: "Auflösung Feuchte", 				options: aufloesung,									description: "Default: Keine Nachkommastelle", 	defaultValue:0
		input name: "parameter6",	type: "enum",		title: "VOC-on Change Reporting", 			options: vocChangeReport, 								description: "Default: 0.5 ppm", 				defaultValue:5
		input name: "parameter7",	type: "enum",		title: "CO2 Change Reporting", 				options: co2ChangeReport, 								description: "Default: 500 ppm", 				defaultValue:5
		input name: "parameter8",	type: "bool",		title: "Luftgüte per LED signalisieren", 	description: "Default: Luftgüte per Led signalisieren",	defaultValue: true
		input name: "lg", 			type: "bool",		title: "Enable debug message logging", 		description: "",										defaultValue: false
	}
}

@Field static Map commandClassVersions = 
	[0x85:2,	//Association 
	 0x59:1,	//Association Grp Info
	 0x70:1,	//Configuration
	 0x5A:1,	//Device Reset Locally
	 0x7A:3,	//Firmware Update MD (documentiert version 3)
	 0x72:1,	//Manufacturer Specific
	 0x31:10,	//Multilevel Sensor
	 0x71:8,	//Notifikation
	 0x73:1,	//Power Level
	 0x98:2,	//Security
	 0x6C:1,	//Supervision
	 0x55:2,	//Transport Service
	 0x86:2,	//Version
	 0x0A:2]	//Z-wave Plus Info

def parse(String description) {
	def cmd = zwave.parse(description, commandClassVersions)
	if (cmd) { 
		return zwaveEvent(cmd) 
	} else { 
		log.warn "Non-parsed event: ${description}" 
	}
}

void updated() {	
	def cmds = []
	def logmessage = ""
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:1,	size:1,	scaledConfigurationValue: Math.round(parameter1 * 10).toInteger())
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:2,	size:1,	scaledConfigurationValue: Math.round(parameter2).toInteger())
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:3,	size:1,	scaledConfigurationValue: parameter3.toInteger())
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:4,	size:1,	scaledConfigurationValue: parameter4.toInteger())
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:5,	size:1,	scaledConfigurationValue: parameter5.toInteger())
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:6,	size:1,	scaledConfigurationValue: parameter6.toInteger())
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:7,	size:1,	scaledConfigurationValue: parameter7.toInteger())
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: parameter8 ? 1 : 0)
	for ( int i=1 ; i<=8 ; i++) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationGet(parameterNumber:i)
	}
	if (lg) {
		log.info "Updated"
		log.info "Dem Parameter 1 ist den Wert ${Math.round(parameter1 * 10).toInteger()} gesendet"
		log.info "Dem Parameter 2 ist den Wert ${Math.round(parameter2).toInteger()} gesendet"
		log.info "Dem Parameter 3 ist den Wert ${parameter3.toInteger()} gesendet"
		log.info "Dem Parameter 4 ist den Wert ${parameter4.toInteger()} gesendet"
		log.info "Dem Parameter 5 ist den Wert ${parameter5.toInteger()} gesendet"
		log.info "Dem Parameter 6 ist den Wert ${parameter6.toInteger()} gesendet"
		log.info "Dem Parameter 7 ist den Wert ${parameter7.toInteger()} gesendet"
		log.info "Dem Parameter 8 ist den Wert ${parameter8 ? 1 : 0} gesendet"
	}
	sendToDevice (cmds)
}

void installed() {
	log.info "Device ${device.label?device.label:device.name} is installed"
	def cmds = []
	sendEvent(name:"carbonDioxide",value: 0,unit:"ppm")
	sendEvent(name:"temperature",value: 0,unit:"°C")
	sendEvent(name:"humidity",value: 0,unit:"%")
	sendEvent(name:"DewPoint",value: 0)
	sendEvent(name:"VOC",value: 0)
	sendEvent(name:"DeviceResetLocally",value: false)
	cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:1)
	cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:2)
	cmds << new hubitat.zwave.commands.associationv2.AssociationGroupingsGet()
	for ( int i=1 ; i<=8 ; i++) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:i,	defaultValue: true)
		log.info "Parameter nummer ${i} ist zurückgesetzt"
	}
	setDefaultAssociations()
	sendToDevice (cmds)
}

void setDefaultAssociations() {
	def hubitatHubID = String.format('%02x', zwaveHubNodeId.toInteger()).toUpperCase()
	state.defaultG1 = [hubitatHubID]
	state.defaultG2 = []
}

def maxAssociationGroup(){
	if (!state.associationGroups) {
		if (lg) log.info "${device.label?device.label:device.name}: Wird die Anzahl fon unterstützen Associationsgroupen vom Gerät abgefragt"
		sendToDevice(new hubitat.zwave.commands.associationv2.AssociationGroupingsGet())
	}
	(state.associationGroups ? state.associationGroups : 5) as int
}

void zwaveEvent(hubitat.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
	if (lg) log.info "${device.label?device.label:device.name}: Es wird ${cmd.supportedGroupings} Anzahl von Groupen"
	sendEvent(name: "groups", value: cmd.supportedGroupings)
	state.associationGroups = cmd.supportedGroupings
}

void zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
	def temp = []
	if (cmd.nodeId != []) {
		cmd.nodeId.each {
			temp += it.toString().format('%02x', it.toInteger()).toUpperCase()
		}
	}
	state."actualAssociation${cmd.groupingIdentifier}" = temp
	if (lg) log.info "${device.label?device.label:device.name}: Associations für Group ${cmd.groupingIdentifier}: ${temp}"
}

void poll() {
	if (lg) log.info "poll"
	def cmds = []
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:parameter3 == 1 ? 1 : 0,sensorType:0x01)
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:0,sensorType:0x05)
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:parameter3 == 1 ? 1 : 0,sensorType:0x0B)
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:0,sensorType:0x11)
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:1,sensorType:0x27)
	cmds << new hubitat.zwave.commands.notificationv8.NotificationGet(event:0x06,notificationType:0x0D,v1AlarmType:0x00)
	sendToDevice(cmds)
}

void zwaveEvent(hubitat.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
	sendEvent(name:"DeviceResetLocally", value: true)
}

void zwaveEvent (hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelReport cmd) {
	def map = [:]	
	switch (cmd.sensorType) {
		case 0x01:
		map.name = "temperature"
		map.unit = cmd.scale == 1 ? "°F" : "°C"
		map.value = cmd.scaledSensorValue
		break;
		case 0x05:
		map.name = "humidity"			
		map.unit = cmd.scale == 1 ? "g/m^3" : "%"
		map.value = cmd.scaledSensorValue
		break;
		case 0x0B:
		map.name = "DewPoint"
		map.unit = cmd.scale == 1 ? "°F" : "°C"
		map.value = cmd.scaledSensorValue
		break;
		case 0x11:
		map.name = "carbonDioxide"
		map.unit = "ppm"
		map.value = cmd.scaledSensorValue
		if (map.value < 800) {
			notifity ("CO2",0)
		} else if (map.value < 1000) {
			notifity ("CO2",1)
		} else if (map.value < 1400) {
			notifity ("CO2",2)
		} else if (map.value < 2000) {
			notifity ("CO2",3)
		} else {
			notifity ("CO2",4)
		}
		break;
		case 0x27:
		map.name = "VOC"
		map.value = cmd.scaledSensorValue
		if (map.value < 0.065) {
			notifity ("VOC",0)
		} else if (map.value < 0.220) {
			notifity ("VOC",1)
		} else if (map.value < 0.660) {
			notifity ("VOC",2)
		} else if (map.value < 2.200) {
			notifity ("VOC",3)
		} else {
			notifity ("VOC",4)
		} 
		break;
		default:
		log.warn "Sensor Multilevel unhandled Report: ${cmd}"
	}
	if (lg) {log.info "${device.label?device.label:device.name} hat ${map.name} im Wert von ${map.value} gemessen"}
	sendEvent(map)
}

void zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd){
	if (lg) log.info "${cmd}"
	def cmds = []
	switch (cmd.parameterNumber) {
		case 1:
		if (parameter1 != cmd.scaledConfigurationValue / 10) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:1,	size:1,	scaledConfigurationValue: Math.round(parameter1 * 10).toInteger())
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		case 2:
		if (Math.round(parameter2) != cmd.scaledConfigurationValue) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:2,	size:1,	scaledConfigurationValue: Math.round(parameter2).toInteger())
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		case 3:
		if (parameter3.toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:3,	size:1,	scaledConfigurationValue: parameter3.toInteger())
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		case 4:
		if (parameter4.toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:4,	size:1,	scaledConfigurationValue: parameter4.toInteger())
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		case 5:
		if (parameter5.toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:5,	size:1,	scaledConfigurationValue: parameter5.toInteger())
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		case 6:
		if (parameter6.toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:6,	size:1,	scaledConfigurationValue: parameter6.toInteger())
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		case 7:
		if (parameter7.toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:7,	size:1,	scaledConfigurationValue: parameter7.toInteger())
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		case 8:
		if ((parameter8 ? 1 : 0) != cmd.scaledConfigurationValue) {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde nicht übernommen, erneuter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: parameter8 ? 1 : 0)
		} else {
			if (lg) log.info "Configurationsparameter nummer ${cmd.parameterNumber} wurde erfolgreich übernommen"
		}
		break;
		default:
		log.warn "Unhlandled Report ${cmd}"	
	}
	if (cmds != []) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationGet(parameterNumber:cmd.parameterNumber)
		sendToDevice(cmds)
	}
}

void zwaveEvent(hubitat.zwave.commands.notificationv8.NotificationReport cmd) {
	if (cmd.notificationType == 0x0D) {
		if (cmd.event == 0x06) {
			if (cmd.eventParametersLength != 0) {
				switch (cmd.eventParameter[0]) {
				case 0x01:
				sendEvent(name:"Home-Health",value:"gut",displayed:true)
				break;
				case 0x02:
				sendEvent(name:"Home-Health",value:"mittelmäßig",displayed:true)
				break;
				case 0x03:
				sendEvent(name:"Home-Health",value:"gesundheitsschädlich",displayed:true)
				break;
				case 0x04:
				sendEvent(name:"Home-Health",value:"lebensgefahr",displayed:true)
				break;
				}
			} else {
				log.warn "Warn!!! Home-Health liefert wieder falsche Werte!!!"
			}
		} else {
			log.warn "Warnung!!! Home-Health hat falsche Event gemeldet!!!"
		}
	} else {
		log.warn "Warnung!!! Notifikation hat keine Home-Health gemeldet, algemeiner Fehler!"
	}
}

private notifity (String sensor,int value) {
    def msg = [:]
	switch (value) {
		case 0:
		msg.value = "hervorragend"
		break;
		case 1:
		msg.value = "gut"
		break;
		case 2:
		msg.value = "mittelmäßig"
		break;
		case 3:
		msg.value = "gesundheitsschädlich"
		break;
		case 4:
		msg.value = "lebensgefahr"
		break;
		default:
		log.debug "Fehler in vocNotifity"
	}
	if (sensor == "CO2") {
		msg.name = "carbonDioxide-Niveau"
	} else {
		msg.name = "VOC-Niveau"
	}
	if (lg) log.info "Notifikaton is ${msg}"
	sendEvent (msg)
}

void zwaveEvent(hubitat.zwave.Command cmd) {
	log.debug "${device.displayName}: Unhandled: ${cmd}"
}

void sendToDevice(List<hubitat.zwave.Command> cmds, Long delay=300) {
    sendHubCommand(new hubitat.device.HubMultiAction(commands(cmds, delay), hubitat.device.Protocol.ZWAVE))
}

void sendToDevice(hubitat.zwave.Command cmd, Long delay=300) {
    sendHubCommand(new hubitat.device.HubAction(zwaveSecureEncap(cmd.format()), hubitat.device.Protocol.ZWAVE))
}

List<String> commands(List<hubitat.zwave.Command> cmds, Long delay=300) {
    return delayBetween(cmds.collect{ zwaveSecureEncap(it.format()) }, delay)
}

void setAssociationGroup(group, nodes, action, endpoint = null){
	action = "${action}" == "1" ? "Add" : "${action}" == "0" ? "Remove" : "${action}"
	group  = "${group}" =~ /\d+/ ? (group as int) : group
	nodes  = [] + nodes ?: [nodes]
	if (! nodes.every { it =~ /[0-9A-F]+/ }) {
		log.error "${device.label?device.label:device.name}: invalid Nodes ${nodes}"
		return
	}
	if (group < 1 || group > maxAssociationGroup()) {
		log.error "${device.label?device.label:device.name}: Association group is invalid 1 <= ${group} <= ${maxAssociationGroup()}"
		return
	}
	def associations = state."desiredAssociation${group}"?:[]
	nodes.each {
		node = "${it}"
		switch (action) {
			case "Remove":
			if (lg) log.info "${device.label?device.label:device.name}: Removing node ${node} from association group ${group}"
			associations = associations - node
			break
			case "Add":
			if (lg) log.info "${device.label?device.label:device.name}: Adding node ${node} to association group ${group}"
			associations << node
			break
		}
	}
	state."desiredAssociation${group}" = associations.unique()
	processAssociations()
}

def processAssociations(){
	def cmds = []
	setDefaultAssociations()
	def associationGroups = maxAssociationGroup()
	for (int i = 1; i <= associationGroups; i++){
		if(state."actualAssociation${i}" != null){
			if(state."desiredAssociation${i}" != null || state."defaultG${i}") {
				def refreshGroup = false
				((state."desiredAssociation${i}"? state."desiredAssociation${i}" : [] + state."defaultG${i}") - state."actualAssociation${i}").each {
					if (it){
						if (lg) log.info "${device.label?device.label:device.name}: Adding node $it to group $i"
						cmds << new hubitat.zwave.commands.associationv2.AssociationSet(groupingIdentifier:i, nodeId:hubitat.helper.HexUtils.hexStringToInt(it))
						refreshGroup = true
					}
				}
				((state."actualAssociation${i}" - state."defaultG${i}") - state."desiredAssociation${i}").each {
					if (it) {
						if (lg) log.info "${device.label?device.label:device.name}: Removing node $it from group $i"
						cmds << new hubitat.zwave.commands.associationv2.AssociationRemove(groupingIdentifier:i, nodeId:hubitat.helper.HexUtils.hexStringToInt(it))
						refreshGroup = true
					}
				}
				if (refreshGroup) cmds << zwave.associationV2.associationGet(groupingIdentifier:i)
				else if (lg) log.info "${device.label?device.label:device.name}: There are no association actions to complete for group $i"
			}
		} else {
			if (lg) log.info "${device.label?device.label:device.name}: Association info not known for group $i. Requesting info from device."
			cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:i)
		}
	}
	sendToDevice (cmds)
}

def configure() {
}

def setAssociationGroup() {
}

/*
void testNode (pLevel, frameCount, nodeID) {
	if (pLevel < 0 || pLevel > 9) pLevel = 0
	frameCount = frameCount.toInteger()
	if (! nodeID =~ /[0-9A-F]+/) {
		log.error "${device.label?device.label:device.name}: invalid Nodes ${nodes}"
		return
	}
	sendToDevice (new hubitat.zwave.commands.powerlevelv1.PowerlevelTestNodeSet(powerLevel: pLevel.toInteger(), testFrameCount: frameCount.toInteger(), testNodeid: hubitat.helper.HexUtils.hexStringToInt(nodeID)))
	sendEvent(name: "Testergebnis", value: "Anfrage gesendet")
}

void zwaveEvent(hubitat.zwave.commands.powerlevelv1.PowerlevelTestNodeReport cmd) {
	switch (cmd.statusOfOperation) {
		case 0:
		sendEvent(name: "Testergebnis", value: "Failied für die Node ${cmd.testNodeid.toString().format('%02x', cmd.testNodeid.toInteger()).toUpperCase()}")
		break
		case 1:
		sendEvent(name: "Testergebnis", value: "Erfolgreich angekommen ${cmd.testFrameCount} Pakete für die Node ${cmd.testNodeid.toString().format('%02x', cmd.testNodeid.toInteger()).toUpperCase()}")
		break
		case 2:
		break
	}
}
*/
