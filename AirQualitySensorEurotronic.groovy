metadata {
	definition(name: "Eurotronic Sensor", namespace: "aelfot", author: "Ravil Rubashkin") { 
		capability "CarbonDioxideMeasurement"		//carbonDioxide - NUMBER, unit:ppm
		capability "TemperatureMeasurement"			//temperature - NUMBER, unit:°F || °C
		capability "RelativeHumidityMeasurement"	//humidity - NUMBER, unit:%rh
		capability "Sensor"
		capability "Polling"
		
		attribute "VOC",					"number"
		attribute "DewPoint", 				"number"
		attribute "VOC-Niveau", 			"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]
		attribute "carbonDioxide-Niveau", 	"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]
		attribute "Home-Health",			"enum", ["gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]
		attribute "DeviceResetLocally", 	"bool"

		command "setAssociationGroup",	[[name: "Group Number",		type:"ENUM", 	constraints: ["1","2"]], 
										[name: "Z-Wave Node",		type:"NUMBER", 	description: "Enter the node number (Integer) associated with the node"], 
										[name: "Action", 			type:"ENUM", 	constraints: ["Add", "Remove"]]]        

		fingerprint mfr:"0148", prod:"0005"
		fingerprint deviceId: "0001", inClusters: "0x5E, 0x85, 0x70, 0x59, 0x55, 0x31, 0x71, 0x86, 0x72, 0x5A, 0x73, 0x98, 0x9F, 0x6C, 0x7A"
		fingerprint deviceId: "0001", inClusters: "0x5E, 0x6C, 0x55, 0x98, 0x9F", secureInClusters: "0x86, 0x85, 0x70, 0x59, 0x72, 0x31, 0x71, 0x5A, 0x73, 0x7A" 
		}	

	def tempEinheit = [:]
		tempEinheit << ["0":"Celcius"]
		tempEinheit << ["1":"Fahrenheit"]

	def aufloesung = [:]
		aufloesung << ["0":"Keine Nachkommastelle"]
		aufloesung << ["1":"Eine Nachkommastelle"] 
		aufloesung << ["2":"Zwei Nachkommastellen"]

	def vocChangeReport = [:]
		vocChangeReport << ["0":"Keine Meldung"]
		vocChangeReport << ["1":"Meldung bei Delta 0.1 ppm"]
		vocChangeReport << ["2":"Meldung bei Delta 0.2 ppm"]
		vocChangeReport << ["3":"Meldung bei Delta 0.3 ppm"]
		vocChangeReport << ["4":"Meldung bei Delta 0.4 ppm"]
		vocChangeReport << ["5":"Meldung bei Delta 0.5 ppm"]
		vocChangeReport << ["6":"Meldung bei Delta 0.6 ppm"]
		vocChangeReport << ["7":"Meldung bei Delta 0.7 ppm"]
		vocChangeReport << ["8":"Meldung bei Delta 0.8 ppm"]
		vocChangeReport << ["9":"Meldung bei Delta 0.9 ppm"]
		vocChangeReport << ["10":"Meldung bei Delta 1.0 ppm"]

	def co2ChangeReport = [:]
		co2ChangeReport << ["0":"Keine Meldung"]
		co2ChangeReport << ["1":"Meldung bei Delta 100 ppm"]
		co2ChangeReport << ["2":"Meldung bei Delta 200 ppm"]
		co2ChangeReport << ["3":"Meldung bei Delta 300 ppm"]
		co2ChangeReport << ["4":"Meldung bei Delta 400 ppm"]
		co2ChangeReport << ["5":"Meldung bei Delta 500 ppm"]
		co2ChangeReport << ["6":"Meldung bei Delta 600 ppm"]
		co2ChangeReport << ["7":"Meldung bei Delta 700 ppm"]
		co2ChangeReport << ["8":"Meldung bei Delta 800 ppm"]
		co2ChangeReport << ["9":"Meldung bei Delta 900 ppm"]
		co2ChangeReport << ["10":"Meldung bei Delta 1000 ppm"]

	preferences {
        input name: "parameter1",	type: "decimal",	title: "Temperatur on Change Reporting",	description: "0.1 - 5°C. Default: 0.5°C. 0 - off",		defaultValue: 0.5,	range:"0..5.0"
        input name: "parameter2",	type: "decimal",	title: "Feuchtigkeit on Change Reporting",	description: "1-5%. Default: 5%. 0 - off",				defaultValue: 5,	range:"0..10"
        input name: "parameter3",	type: "enum",		title: "Temperatureeinheit", 				options: tempEinheit,						 			description: "Default: °C",						defaultValue:"0"
        input name: "parameter4",	type: "enum",		title: "Auflösung Temperatur",				options: aufloesung, 									description: "Default: Eine Nachkommastelle",	defaultValue:"1"
        input name: "parameter5",	type: "enum", 		title: "Auflösung Feuchte", 				options: aufloesung,									description: "Default: Keine Nachkommastelle", 	defaultValue:"0"
        input name: "parameter6",	type: "enum",		title: "VOC-on Change Reporting", 			options: vocChangeReport, 								description: "Default: 0.5 ppm", 				defaultValue:"5"
        input name: "parameter7",	type: "enum",		title: "CO2 Change Reporting", 				options: co2ChangeReport, 								description: "Default: 500 ppm", 				defaultValue:"5"
        input name: "parameter8",	type: "bool",		title: "Luftgüte per LED signalisieren", 	description: "Default: Luftgüte per Led signalisieren",	defaultValue: true   
		input name: "lg", 			type: "bool",		title: "Enable debug message logging", 		description: "",										defaultValue: false
	}

}
private getCommandClassVersion() {
	[0x85:2,	//Association
	 0x59:1,	//Association Grp Info
	 0x70:1,	//Configuration
	 0x5A:1,	//Device Reset Locally
	 0x7A:1,	//Firmware Update MD
	 0x72:1,	//Manufacturer Specific
	 0x31:10,	//Multilevel Sensor
	 0x71:8,	//Notifikation
	 0x73:1,	//Power Level
	 0x98:2,	//Security
	 0x6C:1,	//Supervision
	 0x55:2,	//Transport Service
	 0x86:2,	//Version
	 0x0A:2]	//Z-wave Plus Info
}

def parse(String description) {
	def cmd = zwave.parse(description, getCommandClassVersion())
	if (cmd) {        
		return zwaveEvent(cmd)
    } else {
		log.debug "Non-parsed event: ${description}"
    }
}

void sendToDevice(List<String> cmds, Long delay=300) {
    sendHubCommand(new hubitat.device.HubMultiAction(commands(cmds, delay), hubitat.device.Protocol.ZWAVE))
}

void sendToDevice(String cmd, Long delay=300) {
    sendHubCommand(new hubitat.device.HubAction(zwaveSecureEncap(cmd), hubitat.device.Protocol.ZWAVE))
}

List<String> commands(List<String> cmds, Long delay=300) {
    return delayBetween(cmds.collect{ zwaveSecureEncap(it) }, delay)
}

void updated() {
	if (lg) {log.debug "Updated"}
	def cmds = []
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:1,	size:1,	scaledConfigurationValue: Math.round(parameter1 * 10)).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:2,	size:1,	scaledConfigurationValue: Math.round(parameter2)).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:3,	size:1,	scaledConfigurationValue: parameter3.toInteger()).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:4,	size:1,	scaledConfigurationValue: parameter4.toInteger()).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:5,	size:1,	scaledConfigurationValue: parameter5.toInteger()).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:6,	size:1,	scaledConfigurationValue: parameter6.toInteger()).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:7,	size:1,	scaledConfigurationValue: parameter7.toInteger()).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: parameter8 ? 1 : 0).format()
	if (state.actualAssociation1 == null || state.actualAssociation2 == null) {
		cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:1)
		cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:2)
	}
	sendToDevice(cmds)
}

void installed() {
	def cmds = []
	sendEvent(name:"carbonDioxide",value: 0,unit:"ppm")
	sendEvent(name:"temperature",value: 0,unit:"°C")
	sendEvent(name:"humidity",value: 0,unit:"%")
	sendEvent(name:"DewPoint",value: 0)
	sendEvent(name:"VOC",value: 0)
	sendEvent(name:"DeviceResetLocally",value: false)
	cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:1)
	cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:2)
}

def poll() {
	def cmds = []
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:parameter3 == "1" ? 1 : 0,sensorType:0x01).format()
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:0,sensorType:0x05).format()
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:parameter3 == "1" ? 1 : 0,sensorType:0x0B).format()
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:0,sensorType:0x11).format()
	cmds << new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelGet(scale:1,sensorType:0x27).format()
	cmds << new hubitat.zwave.commands.notificationv8.NotificationGet(event:0x06,notificationType:0x0D,v1AlarmType:0x00).format()
	sendToDevice(cmds)
}

def zwaveEvent(hubitat.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
	sendEvent(name:"DeviceResetLocally", value: true)
}

def zwaveEvent (hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelReport cmd) {
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
			notifity ("CO2",1)
		} else if (map.value < 1000) {
			notifity ("CO2",2)
		} else if (map.value < 1400) {
			notifity ("CO2",3)
		} else if (map.value < 2000) {
			notifity ("CO2",4)
		} else {
			notifity ("CO2",5)
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
		} else if (map.value < 5.500) {
			notifity ("VOC",4)
		} else { 
			notifity ("VOC",5)
		}           
		break;
		default:
		log.debug "Sensor Multilevel unhandled Report: ${cmd}"
	}
	if (lg) {log.debug "${device} hat ${map} gemessen"}
	createEvent(map)
}

def zwaveEvent(hubitat.zwave.commands.notificationv8.NotificationReport cmd) {
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
		msg.value = "schlecht"
		break;    
		case 4:    
		msg.value = "gesundheitsschädlich"
		break;    
		case 5:    
		msg.value = "lebensgefahr"
		break;
		default :
		log.debug "Fehler in vocNotifity"
	}
	if (sensor == "CO2") {
		msg.name = "carbonDioxide-Niveau"
	} else {
		msg.name = "VOC-Niveau"
	}
	sendEvent (msg)
}

def zwaveEvent(hubitat.zwave.Command cmd) {
	log.debug "${device.displayName}: Unhandled: $cmd"
}

def setAssociationGroup(group, nodes, action){
	action = action == 1 ? "Add" : action == 0 ? "Remove": action
	group = group == 0 ? 1 : 2
	maxAssoz = group == 1 ? 1 : 5
	def cmds = []
	count = 0
		
    if (! nodes.every { it =~ /[0-9]/ }) {
        log.error "${device.label?device.label:device.name}: invalid Nodes ${nodes}"
        return
    }    	
	if (state."actualAssociation${group}" != null) {
		count = state."actualAssociation${group}".size()
	} 
    	
	if (nodes != null) {
		nodes.each {
			log.debug "aktion ist $action"
			switch (action) {
				case "Add"	:
				if (count >= maxAssoz) {
					log.warn "Group $group ist voll. Keine Aufnahme mehr möglich"
				} else {	
					log.debug "Add von $it"
					cmds << new hubitat.zwave.commands.associationv2.AssociationSet(groupingIdentifier:group,nodeId:it.toInteger())						
					count = count + 1
				}
				break;
				case "Remove":
				if (count <= 0) {
					log.warn "Group $group ist leer."
				} else {
					log.debug "Del von $it"
					cmds << new hubitat.zwave.commands.associationv2.AssociationRemove(groupingIdentifier:group, nodeId:it.toInteger())
					count =count - 1
				}
				break;
				default:
				log.debug "action nicht gefunden"
			}
		}
	} else {
		log.debug "nodes ist null"
	}	
	cmds << new hubitat.zwave.commands.associationv2.AssociationGet(groupingIdentifier:group)
	sendToDevice(cmds)
}

def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
	def temp = []
	if (cmd.nodeId != []) {
		cmd.nodeId.each {
			temp << it.toInteger()
		}
	} 
	state."actualAssociation${cmd.groupingIdentifier}" = temp
	log.info "${device.label?device.label:device.name}: Associations for Group ${cmd.groupingIdentifier}: ${temp}"
}
