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
        capability "Health Check"
        capability "Notification"
        capability "Configuration"
        capability "Polling"
                
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
			state("temperature", label: '${currentValue}°',unit: "",
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
		state "dewpoint", label:'${currentValue}', unit: "°C"
	}
        valueTile("tvocLevel", "device.tvocLevel", inactiveLabel: false, width: 2, height: 2) {
		state "tvocLevel", label: '${currentValue}', unit: ""
	}
                        
        main "dewpoint"
	details(["humidity", "temperature", "carbonDioxide", "dewpoint", "tvocLevel", "carbonDioxideHealthConcern", "tvocHealthConcern"])
	}

    def tempReportRates = [:]
        tempReportRates << ["0"  :"Disabled"]                               //0x00
        tempReportRates << ["1"  : "Report 0.1 degree temperature change"]  //0x01
        tempReportRates << ["2"  : "Report 0.2 degree temperature change"]  //0x02
        tempReportRates << ["3"  : "Report 0.3 degree temperature change"]  //0x03
        tempReportRates << ["4"  : "Report 0.4 degree temperature change"]  //0x04
        tempReportRates << ["5"  : "Report 0.5 degree temperature change"]  //0x05
        tempReportRates << ["6"  : "Report 0.6 degree temperature change"]  //0x06
        tempReportRates << ["7"  : "Report 0.7 degree temperature change"]  //0x07
        tempReportRates << ["8"  : "Report 0.8 degree temperature change"]  //0x08
        tempReportRates << ["9"  : "Report 0.9 degree temperature change"]  //0x09
        tempReportRates << ["10" : "Report 10 degree temperature change"]   //0x0A

    def humReportRates = [:]
        humReportRates << ["0" :"Disabled"]                             //0x00
        humReportRates << ["1" :"Report 1 percent humidity change"]     //0x01
        humReportRates << ["2" :"Report 1 percent humidity change"]     //0x02
        humReportRates << ["3" :"Report 1 percent humidity change"]     //0x03
        humReportRates << ["4" :"Report 1 percent humidity change"]     //0x04
        humReportRates << ["5" :"Report 1 percent humidity change"]     //0x05
        humReportRates << ["6" :"Report 1 percent humidity change"]     //0x06
        humReportRates << ["7" :"Report 1 percent humidity change"]     //0x07
        humReportRates << ["8" :"Report 1 percent humidity change"]     //0x08
        humReportRates << ["9" :"Report 1 percent humidity change"]     //0x09
        humReportRates << ["10":"Report 1 percent humidity change"]     //0x0A

    def tempEinheit = [:]
        tempEinheit << ["0":"Celcius"]          //0x00
        tempEinheit << ["1":"Fahrenheit"]       //0x01

    def tempAufloesung = [:]
        tempAufloesung << ["0":"Keine Nachkommastelle"] //0x00
        tempAufloesung << ["1":"Eine Nachkommastelle"]  //0x00
        tempAufloesung << ["2":"Zwei Nachkommastellen"] //0x00

    def humAufloesung = [:]
        humAufloesung << ["0":"Keine Nachkommastelle"]  //0x00
        humAufloesung << ["1":"Eine Nachkommastelle"]   //0x01
        humAufloesung << ["2":"Zwei Nachkommastellen"]  //0x02

    def vocChangeReport = [:]
        vocChangeReport << ["0":"Keine Meldung"]                    //0x00
        vocChangeReport << ["1":"Meldung bei Änderung um 0.1 ppm"]  //0x01
        vocChangeReport << ["2":"Meldung bei Änderung um 0.2 ppm"]  //0x02
        vocChangeReport << ["3":"Meldung bei Änderung um 0.3 ppm"]  //0x03
        vocChangeReport << ["4":"Meldung bei Änderung um 0.4 ppm"]  //0x04
        vocChangeReport << ["5":"Meldung bei Änderung um 0.5 ppm"]  //0x05
        vocChangeReport << ["6":"Meldung bei Änderung um 0.6 ppm"]  //0x06
        vocChangeReport << ["7":"Meldung bei Änderung um 0.7 ppm"]  //0x07
        vocChangeReport << ["8":"Meldung bei Änderung um 0.8 ppm"]  //0x08
        vocChangeReport << ["9":"Meldung bei Änderung um 0.9 ppm"]  //0x09
        vocChangeReport << ["10":"Meldung bei Änderung um 1.0 ppm"] //0x0A

    def co2ChangeReport = [:]
        co2ChangeReport << ["0":"Keine Meldung"]                    //0x00
        co2ChangeReport << ["1":"Meldung bei Änderung um 0.1 ppm"]  //0x01
        co2ChangeReport << ["2":"Meldung bei Änderung um 0.2 ppm"]  //0x02
        co2ChangeReport << ["3":"Meldung bei Änderung um 0.3 ppm"]  //0x03
        co2ChangeReport << ["4":"Meldung bei Änderung um 0.4 ppm"]  //0x04
        co2ChangeReport << ["5":"Meldung bei Änderung um 0.5 ppm"]  //0x05
        co2ChangeReport << ["6":"Meldung bei Änderung um 0.6 ppm"]  //0x06
        co2ChangeReport << ["7":"Meldung bei Änderung um 0.7 ppm"]  //0x07
        co2ChangeReport << ["8":"Meldung bei Änderung um 0.8 ppm"]  //0x08
        co2ChangeReport << ["9":"Meldung bei Änderung um 0.9 ppm"]  //0x09
        co2ChangeReport << ["10":"Meldung bei Änderung um 1.0 ppm"] //0x0A

    def ledIndikator = [:]
        ledIndikator << ["0":"Led aus"] //0x00
        ledIndikator << ["1":"Led ein"] //0x01

   
    preferences {
        input "Temperaturdifferenz",     "enum", title: "Temperatur on Change Reporting", 		options: tempReportRates, 	description: "Default: 0.5°C", 							required: false, displayDuringSetup: true
        input "Feuchtigkeitsdifferenz",  "enum", title: "Feuchtigkeit on Change Reporting",		options: humReportRates, 	description: "Default: 5%", 							required: false, displayDuringSetup: true
        input "Temperatureeinheit",      "enum", title: "Temperatureeinheit", 				options: tempEinheit, 		description: "Default: °C", 							required: false, displayDuringSetup: true
        input "TemperaturAufloesung",    "enum", title: "Auflösung Temperatur", 			options: tempAufloesung, 	description: "Default: Eine Nachkommastelle", 					required: false, displayDuringSetup: true
        input "FeuchtigkeitsAufloesung", "enum", title: "Auflösung Feuchte", 				options: humAufloesung, 	description: "Default: Keine Nachkommastelle", 					required: false, displayDuringSetup: true
        input "VOCChageReporting",       "enum", title: "VOC-on Change Reporting", 			options: vocChangeReport, 	description: "Default: 0.5 ppm", 						required: false, displayDuringSetup: true
        input "CO2ChangeReporting",      "enum", title: "CO2 Change Reporting", 			options: co2ChangeReport, 	description: "Default: 500 ppm", 						required: false, displayDuringSetup: true
        input "LedIndikation",           "enum", title: "Luftgüte per LED signalisieren", 		options: ledIndikator, 		description: "Default: Luftgüte per Led signalisieren", 			required: false, displayDuringSetup: true   
		
    }

}

def installed() {
	sendEvent(name: "dewpoint", value: 0, unit: "C", linkText:'$device.displayName', descriptionText: "$device.displayName Dew Point is $value", isStateChange:true, displayed:true)
    	sendEvent(name: "carbonDioxideHealthConcern", value: "good", linkText:'$device.displayName', descriptionText: "$device.displayName Hohe Raumluftqualität", isStateChange:true, displayed:true)
	sendEvent(name: "tvocHealthConcern", value: "good", linkText:'$device.displayName', descriptionText: "$device.displayName Sauber", isStateChange:true, displayed:true)   
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
	response([
		secureSequence(zwave.notificationV3.notificationGet(notificationType: 0x0D)), 	// Home Health
		"delay 500",
		secureSequence(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x01, scale: tmpE)), // temperature
		"delay 500",
        	secureSequence(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x05, scale: 0)), // humidity
		"delay 500",
		secureSequence(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x0B, scale: tmpE)), // dewpoint
		"delay 500",
        	secureSequence(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x11, scale: 0)), // CO2
		"delay 500",
        	secureSequence(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x27, scale: 1)), // VOC
		"delay 10000",
		secureSequence(zwave.wakeUpV2.wakeUpNoMoreInformation())
	])     
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

def zwaveEvent(physicalgraph.zwave.commands.securityv1.SecurityMessageEncapsulation	 cmd) { // Devices that support the Security command class can send messages in an encrypted form; they arrive wrapped in a SecurityMessageEncapsulation command and must be unencapsulated
	log.debug "raw secEncap $cmd"
	state.sec = 1
	def encapsulatedCommand = cmd.encapsulatedCommand ([0x20: 1, 0x80: 1, 0x70: 1, 0x72: 1, 0x31: 5, 0x26: 3, 0x75: 1, 0x40: 2, 0x43: 2, 0x86: 1, 0x71: 3, 0x98: 2, 0x7A: 1 ]) 
	if (encapsulatedCommand) {
		return zwaveEvent(encapsulatedCommand)
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
			map.unit = cmd.scale == 1 ? "°F" : "°C"
			map.value = cmd.scaledSensorValue.toFloat()
			break;
		case 5:
			map.name = "humidity"			
			map.unit = cmd.scale == 0 ? "%" : "g/m^3"
            map.value = cmd.scaledSensorValue.toFloat()
			break;
		case 0xB:
        	map.name = "dewpoint"
            map.unit = cmd.scale == 1 ? "°F" : "°C"
            map.value = cmd.scaledSensorValue.toFloat()
           	break;
        case 0x11:
        	map.name = "carbonDioxide"
            map.unit = cmd.scale == 0 ? "Parts/Million" : ""
            map.value = cmd.scaledSensorValue.toInteger() 
            sensorcarbonDioxideHealthConcernLevel (map.value)
            break;
		case 0x27:
			map.name = "tvocLevel"
            map.unit = cmd.scale == 1 ? "Parts/Million" : "mol/m^3"
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
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
	def result = createEvent(descriptionText: "$device.displayName woke up", isStateChange: false)
    cmds += secure(physicalgraph.zwave.commands.notificationv3.NotificationGet(notificationType: 0x0D))
    cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x27, scale: 1))
    cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x11, scale: 0))
    cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x0B, scale: tmpE))
	cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x05, scale: 0))
	cmds += secure(zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType: 0x01, scale: tmpE))
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

def secure(physicalgraph.zwave.Command cmd) {
	if (state.sec) { zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format() } 
		else { cmd.format() }
}

def secureSequence(commands, delay=1500) {
	sendHubCommand(commands.collect{ response(secure(it)) }, delay)
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
	def result = []
	if (cmd.nodeId.any { it == zwaveHubNodeId }) {
		result << sendEvent(descriptionText: "$device.displayName is associated in group ${cmd.groupingIdentifier}")
	} else if (cmd.groupingIdentifier == 1) {
		result << sendEvent(descriptionText: "Associating $device.displayName in group ${cmd.groupingIdentifier}")
		result << response(zwave.associationV1.associationSet(groupingIdentifier:cmd.groupingIdentifier, nodeId:zwaveHubNodeId))
	}
	log.info "Report Received : $cmd"
	result
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
	if (cmd.manufacturerName) { updateDataValue("manufacturer", cmd.manufacturerName) }
	if (cmd.productTypeId) { updateDataValue("productTypeId", cmd.productTypeId.toString()) }
	if (cmd.productId) { updateDataValue("productId", cmd.productId.toString()) }
	if (cmd.manufacturerId){ updateDataValue("manufacturerId", cmd.manufacturerId.toString()) }
	log.info "Report Received : $cmd"
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd ) {
	log.info "Report Received : $cmd"
	def events = []
	switch (cmd.parameterNumber) {
		case 1:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "Temperatur nicht bei Änderungen melden" }
		if (cmd.scaledConfigurationValue == 1) { setValue = "Meldung bei Temperaturdifferenz von 0.1°" }
        if (cmd.scaledConfigurationValue == 2) { setValue = "Meldung bei Temperaturdifferenz von 0.2°" }
        if (cmd.scaledConfigurationValue == 3) { setValue = "Meldung bei Temperaturdifferenz von 0.3°" }
        if (cmd.scaledConfigurationValue == 4) { setValue = "Meldung bei Temperaturdifferenz von 0.4°" }
        if (cmd.scaledConfigurationValue == 5) { setValue = "Meldung bei Temperaturdifferenz von 0.5°" }
        if (cmd.scaledConfigurationValue == 6) { setValue = "Meldung bei Temperaturdifferenz von 0.6°" }
        if (cmd.scaledConfigurationValue == 7) { setValue = "Meldung bei Temperaturdifferenz von 0.7°" }
        if (cmd.scaledConfigurationValue == 8) { setValue = "Meldung bei Temperaturdifferenz von 0.8°" }
        if (cmd.scaledConfigurationValue == 9) { setValue = "Meldung bei Temperaturdifferenz von 0.9°" }
        if (cmd.scaledConfigurationValue == 10) { setValue = "Meldung bei Temperaturdifferenz von 1°" }
		log.info "Temperaturdifferenz: ${setValue}"
		break;
				
		case 2:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "Feuchtigkeit nicht bei Änderungen melden" }
		if (cmd.scaledConfigurationValue == 1) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 1%" }
        if (cmd.scaledConfigurationValue == 2) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 2%" }
        if (cmd.scaledConfigurationValue == 3) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 3%" }
        if (cmd.scaledConfigurationValue == 4) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 4%" }
        if (cmd.scaledConfigurationValue == 5) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 5%" }
        if (cmd.scaledConfigurationValue == 6) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 6%" }
        if (cmd.scaledConfigurationValue == 7) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 7%" }
        if (cmd.scaledConfigurationValue == 8) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 8%" }
        if (cmd.scaledConfigurationValue == 9) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 9%" }
        if (cmd.scaledConfigurationValue == 10) { setValue = "Meldung bei Feuchtigkeitsdifferenz von 10%" }
		log.info "Feuchtigkeitsdifferenz: ${setValue}"
		break;
				
		case 3:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "°C" }
		if (cmd.scaledConfigurationValue == 1) { setValue = "°F" }
		log.info "Temperatureinheit: ${setValue}"
		break;
				
		case 4:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "keine Nachkommastelle" }
		if (cmd.scaledConfigurationValue == 1) { setValue = "eine Nachkommastelle" }
        if (cmd.scaledConfigurationValue == 2) { setValue = "zwei Nachkommastellen" }
		log.info "Temperaturauflösung: ${setValue}"
		break;
				
		case 5:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "keine Nachkommastelle" }
		if (cmd.scaledConfigurationValue == 1) { setValue = "eine Nachkommastelle" }
        if (cmd.scaledConfigurationValue == 2) { setValue = "zwei Nachkommastellen" }
		log.info "Feuchtigkeitsauflösung: ${setValue}"
		break;
				
		case 6:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "Disabled" }
		if (cmd.scaledConfigurationValue == 1) { setValue = "Meldung bei VOC-Änderung von 0.100 ppm" }
		if (cmd.scaledConfigurationValue == 2) { setValue = "Meldung bei VOC-Änderung von 0.200 ppm" }
        if (cmd.scaledConfigurationValue == 3) { setValue = "Meldung bei VOC-Änderung von 0.300 ppm" }
        if (cmd.scaledConfigurationValue == 4) { setValue = "Meldung bei VOC-Änderung von 0.400 ppm" }
        if (cmd.scaledConfigurationValue == 5) { setValue = "Meldung bei VOC-Änderung von 0.500 ppm" }
        if (cmd.scaledConfigurationValue == 6) { setValue = "Meldung bei VOC-Änderung von 0.600 ppm" }
        if (cmd.scaledConfigurationValue == 7) { setValue = "Meldung bei VOC-Änderung von 0.700 ppm" }
        if (cmd.scaledConfigurationValue == 8) { setValue = "Meldung bei VOC-Änderung von 0.800 ppm" }
        if (cmd.scaledConfigurationValue == 9) { setValue = "Meldung bei VOC-Änderung von 0.900 ppm" }
        if (cmd.scaledConfigurationValue == 10) { setValue = "Meldung bei VOC-Änderung von 1.000 ppm" }
		log.info "VOC-s Report: ${setValue}"
		break;
				
		case 7:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "Disabled" }
		if (cmd.scaledConfigurationValue == 1) { setValue = "Meldung bei CO2-Änderung von 100 ppm" }
		if (cmd.scaledConfigurationValue == 2) { setValue = "Meldung bei CO2-Änderung von 200 ppm" }
        if (cmd.scaledConfigurationValue == 3) { setValue = "Meldung bei CO2-Änderung von 300 ppm" }
        if (cmd.scaledConfigurationValue == 4) { setValue = "Meldung bei CO2-Änderung von 400 ppm" }
        if (cmd.scaledConfigurationValue == 5) { setValue = "Meldung bei CO2-Änderung von 500 ppm" }
        if (cmd.scaledConfigurationValue == 6) { setValue = "Meldung bei CO2-Änderung von 600 ppm" }
        if (cmd.scaledConfigurationValue == 7) { setValue = "Meldung bei CO2-Änderung von 700 ppm" }
        if (cmd.scaledConfigurationValue == 8) { setValue = "Meldung bei CO2-Änderung von 800 ppm" }
        if (cmd.scaledConfigurationValue == 9) { setValue = "Meldung bei CO2-Änderung von 900 ppm" }
        if (cmd.scaledConfigurationValue == 10) { setValue = "Meldung bei CO2-Änderung von 1000 ppm" }
		log.info "CO2 Report: ${setValue}"
		break;
				
		case 8:
		def setValue
		if (cmd.scaledConfigurationValue == 0) { setValue = "Disabled" }
        if (cmd.scaledConfigurationValue == 1) { setValue = "Enabled" }
		log.info "Led: ${setValue}"
		break;
	}
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
	log.debug "$cmd"
	def zWaveLibraryTypeDisp  = String.format("%02X",cmd.zWaveLibraryType)
	def zWaveLibraryTypeDesc  = ""
	switch(cmd.zWaveLibraryType) {
		case 1:
		zWaveLibraryTypeDesc = "Static Controller"
		break

		case 2:
		zWaveLibraryTypeDesc = "Controller"
		break

		case 3:
		zWaveLibraryTypeDesc = "Enhanced Slave"
		break

		case 4:
		zWaveLibraryTypeDesc = "Slave"
		break

		case 5:
		zWaveLibraryTypeDesc = "Installer"
		break

		case 6:
		zWaveLibraryTypeDesc = "Routing Slave"
		break

		case 7:
		zWaveLibraryTypeDesc = "Bridge Controller"
		break

		case 8:
		zWaveLibraryTypeDesc = "Device Under Test (DUT)"
		break

		case 0x0A:
		zWaveLibraryTypeDesc = "AV Remote"
		break

    	case 0x0B:
		zWaveLibraryTypeDesc = "AV Device"
		break

		default:
		zWaveLibraryTypeDesc = "N/A"
	}
	def applicationVersionDisp = String.format("%d.%02d",cmd.applicationVersion,cmd.applicationSubVersion)
	def zWaveProtocolVersionDisp = String.format("%d.%02d",cmd.zWaveProtocolVersion,cmd.zWaveProtocolSubVersion)
	sendEvent([name: "applicationVersion", value:  applicationVersionDisp])
	sendEvent([name: "zWaveLibraryType", value:  zWaveLibraryTypeDesc])
} 

def configure() {
	def cmds = []
	def tmpE = Temperatureeinheit == "1" ? 1 : 0	
	cmds << zwave.configurationV1.configurationSet(configurationValue:  Temperaturdifferenz == "0" ? [0x00] : Temperaturdifferenz == "1" ? [0x01] : Temperaturdifferenz == "2" ? [0x02] : Temperaturdifferenz == "3" ? [0x03] : Temperaturdifferenz == "4" ? [0x04] : Temperaturdifferenz == "10" ? [0x0A] : Temperaturdifferenz == "6" ? [0x06] : Temperaturdifferenz == "7" ? [0x07] : Temperaturdifferenz == "8" ? [0x08] : Temperaturdifferenz == "9" ? [0x09] : [0x05], 								parameterNumber:1, size:1, scaledConfigurationValue:  Temperaturdifferenz == "0" ? 0x00 : Temperaturdifferenz == "1" ? 0x01 : Temperaturdifferenz == "2" ? 0x02 : Temperaturdifferenz == "3" ? 0x03 : Temperaturdifferenz == "4" ? 0x04 : Temperaturdifferenz == "10" ? 0x0A : Temperaturdifferenz == "6" ? 0x06 : Temperaturdifferenz == "7" ? 0x07 : Temperaturdifferenz == "8" ? 0x08 : Temperaturdifferenz == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  Feuchtigkeitsdifferenz == "0" ? [0x00] : Feuchtigkeitsdifferenz == "1" ? [0x01] : Feuchtigkeitsdifferenz == "2" ? [0x02] : Feuchtigkeitsdifferenz == "3" ? [0x03] : Feuchtigkeitsdifferenz == "4" ? [0x04] : Feuchtigkeitsdifferenz == "10" ? [0x0A] : Feuchtigkeitsdifferenz == "6" ? [0x06] : Feuchtigkeitsdifferenz == "7" ? [0x07] : Feuchtigkeitsdifferenz == "8" ? [0x08] : Feuchtigkeitsdifferenz == "9" ? [0x09] : [0x05], 	parameterNumber:2, size:1, scaledConfigurationValue:  Feuchtigkeitsdifferenz == "0" ? 0x00 : Feuchtigkeitsdifferenz == "1" ? 0x01 : Feuchtigkeitsdifferenz == "2" ? 0x02 : Feuchtigkeitsdifferenz == "3" ? 0x03 : Feuchtigkeitsdifferenz == "4" ? 0x04 : Feuchtigkeitsdifferenz == "10" ? 0x0A : Feuchtigkeitsdifferenz == "6" ? 0x06 : Feuchtigkeitsdifferenz == "7" ? 0x07 : Feuchtigkeitsdifferenz == "8" ? 0x08 : Feuchtigkeitsdifferenz == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  Temperatureeinheit == "1" ? [0x01] : [0x00], 																																																																																														parameterNumber:3, size:1, scaledConfigurationValue:  Temperatureeinheit == "1" ? 0x01 : 0x00)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  TemperaturAufloesung == "0" ? [0x00] : TemperaturAufloesung == "2" ? [0x02] : [0x01], 																																																																																				parameterNumber:4, size:1, scaledConfigurationValue:  TemperaturAufloesung == "0" ? 0x00 : TemperaturAufloesung == "2" ? 0x02 : 0x01)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  FeuchtigkeitsAufloesung == "2" ? [0x02] : FeuchtigkeitsAufloesung == "1" ? [0x01] : [0x00], 																																																																																		parameterNumber:5, size:1, scaledConfigurationValue:  FeuchtigkeitsAufloesung == "2" ? 0x02 : FeuchtigkeitsAufloesung == "1" ? 0x01 : 0x00)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  VOCChageReporting == "0" ? [0x00] : VOCChageReporting == "1" ? [0x01] : VOCChageReporting == "2" ? [0x02] : VOCChageReporting == "3" ? [0x03] : VOCChageReporting == "4" ? [0x04] : VOCChageReporting == "10" ? [0x0A] : VOCChageReporting == "6" ? [0x06] : VOCChageReporting == "7" ? [0x07] : VOCChageReporting == "8" ? [0x08] : VOCChageReporting == "9" ? [0x09] : [0x05], 													parameterNumber:6, size:1, scaledConfigurationValue:  VOCChageReporting == "0" ? 0x00 : VOCChageReporting == "1" ? 0x01 : VOCChageReporting == "2" ? 0x02 : VOCChageReporting == "3" ? 0x03 : VOCChageReporting == "4" ? 0x04 : VOCChageReporting == "10" ? 0x0A : VOCChageReporting == "6" ? 0x06 : VOCChageReporting == "7" ? 0x07 : VOCChageReporting == "8" ? 0x08 : VOCChageReporting == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  CO2ChangeReporting == "0" ? [0x00] : CO2ChangeReporting == "1" ? [0x01] : CO2ChangeReporting == "2" ? [0x02] : CO2ChangeReporting == "3" ? [0x03] : CO2ChangeReporting == "4" ? [0x04] : CO2ChangeReporting == "10" ? [0x0A] : CO2ChangeReporting == "6" ? [0x06] : CO2ChangeReporting == "7" ? [0x07] : CO2ChangeReporting == "8" ? [0x08] : CO2ChangeReporting == "9" ? [0x09] : [0x05], 											parameterNumber:7, size:1, scaledConfigurationValue:  CO2ChangeReporting == "0" ? 0x00 : CO2ChangeReporting == "1" ? 0x01 : CO2ChangeReporting == "2" ? 0x02 : CO2ChangeReporting == "3" ? 0x03 : CO2ChangeReporting == "4" ? 0x04 : CO2ChangeReporting == "10" ? 0x0A : CO2ChangeReporting == "6" ? 0x06 : CO2ChangeReporting == "7" ? 0x07 : CO2ChangeReporting == "8" ? 0x08 : CO2ChangeReporting == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  LedIndikation == "0" ? [0x00] : [0x01], 																																																																																															parameterNumber:8, size:1, scaledConfigurationValue:  LedIndikation == "0" ? 0x00 : 0x01)
	
	cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     	//get temperature
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x05, scale: 0)     	//get humidity
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     	//get dewpoint
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x11, scale: 0)     	//get carbon dioxide
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x27, scale: 1)     	//get voc
    
	cmds << zwave.configurationV1.configurationGet(parameterNumber:1)               //get pamam - 1..8
	cmds << zwave.configurationV1.configurationGet(parameterNumber:2)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:3)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:4)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:5)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:6)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:7)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:8)
	
    cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet()                  //fingerprint
	cmds << zwave.versionV1.versionGet()
	sendEvent(name: "configuration", value: "sent", displayed: true)
	sendEvent(name: "configure", displayed: false)
	secureSequence(cmds)
}

def updated() {
	sendEvent(name: "checkInterval", value: 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
	if (!state.updatedLastRanAt || new Date().time >= state.updatedLastRanAt + 2000) {
		state.updatedLastRanAt = new Date().time
		unschedule(poll)
		log.trace "Configuring settings"
		runIn (05, configure)
		runEvery1Minute(poll)
		log.info "Refresh Scheduled for every minute"
	} else {
	    log.warn "update ran within the last 2 seconds"
	}
}

def poll() { // If you add the Polling capability to your device type, this command will be called approximately every 5 minutes to check the device's state
	def cmds = []
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     	//get temperature
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x05, scale: 0)     	//get humidity
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     	//get dewpoint
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x11, scale: 0)     	//get carbon dioxide
    cmds << zwave.sensorMultilevelV5.sensorMultilevelGet(sensorType:0x27, scale: 1)			//get voc
	log.trace "POLL $cmds"
	secureSequence (cmds)
}
