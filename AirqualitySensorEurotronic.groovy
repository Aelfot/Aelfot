metadata {
	definition(name: "Luftgutesensor Eurotronic", namespace: "forgetiger55122", author: "Ravil Rubashkin", mnmn:"SmartThingsCommunity", vid:"e1fa8d53-5d1a-3d14-9329-fa156189e663" ) { //"9b37cdaf-0cdb-3643-94cf-867460ea67e6" "e1fa8d53-5d1a-3d14-9329-fa156189e663"
	  capability "forgetiger55122.dewPoint"
    capability "Carbon Dioxide Measurement"
    capability "Tvoc Measurement"
		capability "Temperature Measurement"
		capability "Relative Humidity Measurement"
		capability "Sensor"
    capability "forgetiger55122.tvocHealthConcern"
    capability "Carbon Dioxide Health Concern"
                
		fingerprint mfr:"0148", prod:"0005", model:"0001"
  }

	tiles(scale: 2) {
		multiAttributeTile(name: "tvocHealthConcern", type: "generic", width: 6, height: 6, canChangeIcon: true) {
			tileAttribute("device.tvocHealthConcern", key: "PRIMARY_CONTROL") {
				attributeState("good", label:'${name}', icon: "st.Lighting.light14", backgroundColor: "#153591")
				attributeState("moderate", label:'${name}', icon: "st.Lighting.light11", backgroundColor: "#1e9cbb")
        attributeState("slightlyUnhealthy",, label:'${name}', icon: "st.Lighting.light11", backgroundColor: "#90d2a7")
				attributeState("unhealthy", label:'${name}', icon: "st.Appliances.appliances11", backgroundColor: "#44b621")
        attributeState("veryUnhealthy", label:'${name}', icon: "st.Appliances.appliances11", backgroundColor: "#f1d801")             
      }
      tileAttribute("device.carbonDioxideHealthConcern", key: "SECONDARY_CONTROL") {
        attributeState ("good", label:'${name}', icon: "st.Lighting.light14", backgroundColor: "#153591")
				attributeState ("moderate", label:'${name}', icon: "st.Lighting.light11", backgroundColor: "#1e9cbb")
        attributeState ("slightlyUnhealthy", label:'${name}', icon: "st.Lighting.light11", backgroundColor: "#90d2a7")
				attributeState ("unhealthy", label:'${name}', icon: "st.Appliances.appliances11", backgroundColor: "#44b621")
        attributeState ("veryUnhealthy", label:'${name}', icon: "st.Appliances.appliances11", backgroundColor: "#f1d801")
      }
    }
		valueTile("humidity", "device.humidity", inactiveLabel: false, width: 2, height: 2) {
			state "humidity", label: '${currentValue}%', unit: ""
		}
		valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state("temperature", label: '${currentValue}°',
					backgroundColors: [
							// Celsius
							[value: 0, color: "#153591"],
							[value: 7, color: "#1e9cbb"],
							[value: 15, color: "#90d2a7"],
							[value: 23, color: "#44b621"],
							[value: 28, color: "#f1d801"],
							[value: 35, color: "#d04e00"],
							[value: 37, color: "#bc2323"],
							// Fahrenheit
							[value: 40, color: "#153591"],
							[value: 44, color: "#1e9cbb"],
							[value: 59, color: "#90d2a7"],
							[value: 74, color: "#44b621"],
							[value: 84, color: "#f1d801"],
							[value: 95, color: "#d04e00"],
							[value: 96, color: "#bc2323"]
					])
		}
    valueTile("carbonDioxide", "device.co2Level", inactiveLabel: false, width: 2, height: 2) {
			state "carbonDioxide", label: '${currentValue}', unit: ""
		}
    valueTile("dewpoint", "device.dewpoint", inactiveLabel: false, width: 2, height: 2) {
			state "dewpoint", label:'${currentValue}', unit: ""
		}
    valueTile("tvocLevel", "device.tvocLevel", inactiveLabel: false, width: 2, height: 2) {
			state "tvocLevel", label: '${currentValue}', unit: ""
		}
                        
        /*preferences {
        input "Temperaturdifferenz", "enum", title: "Temperaturdifferenz", options: ["0.1", "0.5", "1"], description: "Report, wenn Temperature geändert 0.1...5,0°C", defaultValue: "0.5" , displayDuringSetup: true
        input "Feuchtigkeitsdifferenz", "number", title: "Feuchtigkeitsdifferenz", description: "Report, wenn Feuchtigkeit geändert 1...100%", defaultValue: 5, range: "1..100", displayDuringSetup: false
        input "Temperatureeinheit", "enum", title: "Temperatureeinheit", options: ["°C", "°F"], defaultValue: "°C", required: false, displayDuringSetup: true
        input "TemperaturAufloesung", "enum", title: "Auflösung Temperatur", options: ["keine Nachkommastelle", "eine Nachkommastelle", "zwei Nachkommastellen"], defaultValue: "eine Nachkommastelle", required: false, displayDuringSetup: true
        input "FeuchtigkeitsAufloesung", "enum", title: "Auflösung Feuchte", options: ["keine Nachkommastelle", "eine Nachkommastelle", "zwei Nachkommastellen"], defaultValue: "keine Nachkommastelle", required: false, displayDuringSetup: true
        input "VOCChageReporting", "number", title: "VOC-on Change Reporting", description: "VOC-on Change Reporting 100...1000 ppb", range: "100..1000",  displayDuringSetup: false
        input "CO2ChangeReporting", "number", title: "CO2 Change Reporting", description: "CO2 Change Reporting 100...1000 ppb", range: "100..1000", displayDuringSetup: false
        input "LedIndikation", "enum", title: "Luftgüte per LED signalisieren", options: ["nein", "ja"], defaultValue: "ja", required: false, displayDuringSetup: true
    	}*/   
		    
		main "dewpoint"
		details(["humidity", "temperature", "carbonDioxide", "dewpoint", "tvocLevel", "carbonDioxideHealthConcern", "tvocHealthConcern"])
	}
}

def installed() {
	sendEvent(name: "dewpoint", value: 0, unit: "C", linkText:'$device.displayName', descriptionText: "$device.displayName Dew Point is $value", isStateChange:true, displayed:true)
  sendEvent(name: "carbonDioxideHealthConcern", value: "good", linkText:'$device.displayName', descriptionText: "$device.displayName Hohe Raumluftqualität", isStateChange:true, displayed:true)
	sendEvent(name: "tvocHealthConcern", value: "good", linkText:'$device.displayName', descriptionText: "$device.displayName Sauber", isStateChange:true, displayed:true)   
	response([
		secure(zwave.notificationV3.notificationGet(notificationType: 0x0D)), // Home Health
		"delay 500",
		secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x01)), // temperature
		"delay 500",
    secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x05)), // humidity
		"delay 500",
		secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x0B)), // dewpoint
		"delay 500",
    secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x11)), // CO2
		"delay 500",
    secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x27)), // VOC
		"delay 10000",
		secure(zwave.wakeUpV2.wakeUpNoMoreInformation())
	])     
}

def updated() {
	configure()
}

def configure() {
	sendEvent(name: "dewpoint", value: 13, unit: "C", linkText:'$device.displayName', descriptionText: "$device.displayName Dew Point is $value", isStateChange:true, displayed:true)
	sendEvent(name: "carbonDioxideHealthConcern", value: "good", linkText:'$device.displayName', descriptionText: "$device.displayName Hohe Raumluftqualität", isStateChange:true, displayed:true)
	sendEvent(name: "tvocHealthConcern", value: "good", linkText:'$device.displayName', descriptionText: "$device.displayName Sauber", isStateChange:true, displayed:true)
	sendEvent(name: "checkInterval", value: 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
  secure(zwave.configurationV1.configurationSet(configurationValue: [0x01] , parameterNumber:1, size:1, scaledConfigurationValue:  0x01))
	secure(zwave.configurationV1.configurationSet(configurationValue: [0x01] , parameterNumber:2, size:1, scaledConfigurationValue:  0x01))
	secure(zwave.configurationV1.configurationSet(configurationValue: [0x00] , parameterNumber:3, size:1, scaledConfigurationValue:  0x00))
	secure(zwave.configurationV1.configurationSet(configurationValue: [0x01] , parameterNumber:4, size:1, scaledConfigurationValue:  0x01))
	secure(zwave.configurationV1.configurationSet(configurationValue: [0x01] , parameterNumber:5, size:1, scaledConfigurationValue:  0x00))
  secure(zwave.configurationV1.configurationSet(configurationValue: [0x01] , parameterNumber:6, size:1, scaledConfigurationValue:  0x01))
  secure(zwave.configurationV1.configurationSet(configurationValue: [0x01] , parameterNumber:7, size:1, scaledConfigurationValue:  0x01))
  secure(zwave.configurationV1.configurationSet(configurationValue: [0x01] , parameterNumber:8, size:1, scaledConfigurationValue:  0x01))
	/*secure(zwave.configurationV1.configurationSet(configurationValue:  Temperaturdifferenz == "0.1" ? [0x01] : Temperaturdifferenz == "0.5" ? [0x05] : [0x0A] , parameterNumber:1, size:1, scaledConfigurationValue:  Temperaturdifferenz == "0.1" ? 0x01 : Temperaturdifferenz == "0.5" ? 0x05 : 0x0A))
	secure(zwave.configurationV1.configurationSet(configurationValue: Feuchtigkeitsdifferenz == null ? [0x05] : [Integer.toHexString(Feuchtigkeitsdifferenz).toUpperCase()], parameterNumber:2, size:1, scaledConfigurationValue:  Feuchtigkeitsdifferenz == null ? 0x05 : Integer.toHexString(Feuchtigkeitsdifferenz).toUpperCase()))
	secure(zwave.configurationV1.configurationSet(configurationValue:  Temperatureeinheit == "°C" ? [0x00] : [0x01], parameterNumber:3, size:1, scaledConfigurationValue:  Temperatureeinheit == "°C" ? 0x00 : 0x01))
	secure(zwave.configurationV1.configurationSet(configurationValue:  TemperaturAufloesung == "keine Nachkommastelle" ? [0x00] : TemperaturAufloesung == "eine Nachkommastelle" ? [0x01] : [0x02], parameterNumber:4, size:1, scaledConfigurationValue:  TemperaturAufloesung == "keine Nachkommastelle" ? 0x00 : TemperaturAufloesung == "eine Nachkommastelle" ? 0x01 : 0x02))
	secure(zwave.configurationV1.configurationSet(configurationValue:  FeuchtigkeitsAufloesung == "keine Nachkommastelle" ? [0x00] : FeuchtigkeitsAufloesung == "eine Nachkommastelle" ? [0x01] : [0x02], parameterNumber:5, size:1, scaledConfigurationValue:  FeuchtigkeitsAufloesung == "keine Nachkommastelle" ? 0x00 : FeuchtigkeitsAufloesung == "eine Nachkommastelle" ? 0x01 : 0x02))
  secure(zwave.configurationV1.configurationSet(configurationValue: VOCChageReporting == null ? [0x05] : [Integer.toHexString(VOCChageReporting-99).toUpperCase()], parameterNumber:6, size:1, scaledConfigurationValue:  VOCChageReporting == null ? 0x05 : VOCChageReporting-99))
  secure(zwave.configurationV1.configurationSet(configurationValue: CO2ChangeReporting == null ? [0x05] : [Integer.toHexString(CO2ChangeReporting-99).toUpperCase()], parameterNumber:7, size:1, scaledConfigurationValue:  CO2ChangeReporting == null ? 0x05 : CO2ChangeReporting-99))//,
  secure(zwave.configurationV1.configurationSet(configurationValue:  LedIndikation == "nein" ? [0x00] : [0x01], parameterNumber:8, size:1, scaledConfigurationValue:  LedIndikation == "nein" ? 0x00 : 0x01))/*/
}

def parse(String description) {
	def results = []
	if (description.startsWith("Err")) {
		results += createEvent(descriptionText: description, displayed: true)
	} else {
		def cmd = zwave.parse(description)
		if (cmd) {
			results += zwaveEvent(cmd)
		}
	}
    log.debug "parse() result ${results.inspect()}"
	return results
}

def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
	def encapsulatedCommand = cmd.encapsulatedCommand()
	if (encapsulatedCommand) {
		zwaveEvent(encapsulatedCommand)
	} else {
		log.warn "Unable to extract encapsulated cmd from $cmd"
		createEvent(descriptionText: cmd.toString())
	}
}

def zwaveEvent(physicalgraph.zwave.commands.notificationv3.EventSupportedReport cmd) {
	switch (cmd.event) {
		case 1:
			createEvent(name:"tvocHealthConcern", value:"good", linkText:'$device.displayName', descriptionText:"$device.displayName Zielwert", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist super")
            break;
		case 2:
			createEvent(name:"tvocHealthConcern", value:"moderate", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften wird empfohlen", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist nicht perfekt. Lüften empfohlen")
            break;
		case 3:
        	createEvent(name:"tvocHealthConcern", value:"slightlyUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften notwendig", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist schlecht. Verstärktes Lüften notwendig")
            break;
        case 4:
        	createEvent(name:"tvocHealthConcern", value:"unhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist miserabel. Raum nutzen nur, wenn unvermeidbar. Lüften!!!")
            break;
        case 5:
        	createEvent(name:"tvocHealthConcern", value:"veryUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist lebensgefährlich. Raum verlassen und lüften.")
            break;    
        }
}

def zwaveEvent(physicalgraph.zwave.commands.notificationv3.NotificationReport cmd) {
	switch (cmd.event) {
		case 1:
			createEvent(name:"tvocHealthConcern", value:"good", linkText:'$device.displayName', descriptionText:"$device.displayName Zielwert", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist super")
            break;
		case 2:
			createEvent(name:"tvocHealthConcern", value:"moderate", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften wird empfohlen", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist nicht perfekt. Lüften empfohlen")
            break;
		case 3:
        	createEvent(name:"tvocHealthConcern", value:"slightlyUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften notwendig", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist schlecht. Verstärktes Lüften notwendig")
            break;
        case 4:
        	createEvent(name:"tvocHealthConcern", value:"unhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist miserabel. Raum nutzen nur, wenn unvermeidbar. Lüften!!!")
            break;
        case 5:
        	createEvent(name:"tvocHealthConcern", value:"veryUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            sendPush("Luftqualität ist lebensgefährlich. Raum verlassen und lüften.")
            break;    
        }
}

def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd) {
	def map = [:]
    switch (cmd.sensorType) {
		case 1:
			map.name = "temperature"
			map.unit = temperatureScale
			map.value = convertTemperatureIfNeeded(cmd.scaledSensorValue, cmd.scale == 1 ? "F" : "C", cmd.precision)
			break;
		case 5:
			map.name = "humidity"			
			map.unit = cmd.scale == 0 ? "%" : "g/m^3"
            map.value = cmd.scaledSensorValue.toFloat()
			break;
		case 0xB:
        	map.name = "dewpoint"
            map.unit = cmd.scale == 1 ? "F" : "C"
            map.value = cmd.scaledSensorValue.toFloat()
           	break;
        case 0x11:
        	map.name = "carbonDioxide"
            map.unit = cmd.scale == 0 ? "Parts/million" : ""
            map.value = cmd.scaledSensorValue.toInteger() 
            sensorcarbonDioxideHealthConcernLevel (map.value)
            break;
		case 0x27:
			map.name = "tvocLevel"
            map.unit = cmd.scale == 1 ? "Parts/million" : "mol/m^3"
            map.value = cmd.scaledSensorValue.toFloat() 
            sensortvocHealthConcernLevel (map.value)
			break;
		default:
			map.descriptionText = cmd.toString()
	}
createEvent(map)
}

def zwaveEvent(physicalgraph.zwave.commands.wakeupv2.WakeUpNotification cmd) {
	def cmds = []
	def result = createEvent(descriptionText: "$device.displayName woke up", isStateChange: false)
    cmds += secure(physicalgraph.zwave.commands.notificationv3.NotificationGet(notificationType: 0x0D))
    cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x27))
    cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x11))
    cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x0B))
	cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x05))
	cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x01))
	cmds += secure(zwave.wakeUpV2.wakeUpNoMoreInformation())
	[result, response(cmds)]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	log.warn "Unhandled command: ${cmd}"
}

def sensortvocHealthConcernLevel (value) {
if (value < 0.065) {
	sensortvocHealthConcern (1)
    } else if (value < 0.22) {
    	sensortvocHealthConcern (1)
        } else if (value < 0.66) {
        	sensortvocHealthConcern (2)
            } else if (value < 2.2) {
            	sensortvocHealthConcern (3)
                } else if (value < 5.5) {
                	sensortvocHealthConcern (4)
                    } else {
                    	sensortvocHealthConcern (5)}
}

def sensorcarbonDioxideHealthConcernLevel  (value) {
if (value < 800) {
	sensorcarbonDioxideHealthConcern (1)
    } else if (value < 1000) {
    	sensorcarbonDioxideHealthConcern (2)
        } else if (value < 1400) {
        	sensorcarbonDioxideHealthConcern (3)
            } else if (value < 2000) {
            	sensorcarbonDioxideHealthConcern (4)
                } else {
                	sensorcarbonDioxideHealthConcern (5)}
}

def sensorcarbonDioxideHealthConcern (value) {
switch (value) {
		case 1:
			sendEvent(name:"carbonDioxideHealthConcern", value:"good", linkText:'$device.displayName', descriptionText:"$device.displayName Zielwert", isStateChange:true, displayed:true)
            break;
		case 2:
			sendEvent(name:"carbonDioxideHealthConcern", value:"moderate", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften wird empfohlen", isStateChange:true, displayed:true)
            break;
		case 3:
        	sendEvent(name:"carbonDioxideHealthConcern", value:"slightlyUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften notwendig", isStateChange:true, displayed:true)
            break;
        case 4:
        	sendEvent(name:"carbonDioxideHealthConcern", value:"unhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            break;
        case 5:
        	sendEvent(name:"carbonDioxideHealthConcern", value:"veryUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            break;     
        }
}

def sensortvocHealthConcern (value){
switch (value) {
		case 1:
			sendEvent(name:"tvocHealthConcern", value:"good", linkText:'$device.displayName', descriptionText:"$device.displayName Zielwert", isStateChange:true, displayed:true)
            break;
		case 2:
			sendEvent(name:"tvocHealthConcern", value:"moderate", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften wird empfohlen", isStateChange:true, displayed:true)
            break;
		case 3:
        	sendEvent(name:"tvocHealthConcern", value:"slightlyUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Verstärktes Lüften notwendig", isStateChange:true, displayed:true)
            break;
        case 4:
        	sendEvent(name:"tvocHealthConcern", value:"unhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            break;
        case 5:
        	sendEvent(name:"tvocHealthConcern", value:"veryUnhealthy", linkText:'$device.displayName', descriptionText:"$device.displayName Nur nutzen, wenn unvermeidbar", isStateChange:true, displayed:true)
            break;    
        }
}

private secure(cmd) {
	if (zwaveInfo.zw.contains("s")) {
		zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
	} else {
		cmd.format()
	}
}
