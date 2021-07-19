metadata {
	definition(name: "Eurotronic Luftgutesensor", namespace: "aelfot", author: "Ravil Rubashkin") { 
	    capability "CarbonDioxideMeasurement"
        capability "Temperature Measurement"
		capability "Relative Humidity Measurement"
		capability "Sensor"
        capability "Configuration"
        capability "TamperAlert"
        capability "Polling"		
        
        attribute "VOC", 					"number"																			//ohne Messeinheit
        attribute "DewPoint", 				"number" 																			//ohne Messeinheit   
        attribute "VOC-Niveau", 			"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]	//Interpretation
        attribute "carbonDioxide-Niveau", 	"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]	//Interpretation
		attribute "Home-Health",			"enum", ["gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]					//Interpretation eventbasiert, LED-Farbe-entsprechend
		attribute "Signal-Stärke",			"enum", ["normal","-1 dBm","-2 dBm","-3 dBm","-4 dBm","-5 dBm","-6 dBm","-7 dBm","-8 dBm","-9 dBm"]
		attribute "Configuration",			"string"
		attribute "groups", "Number"
		
		command "setSignalStaerke", 	[[name:"Signalstärke",			type: "ENUM" , 	constraints: ["normal","-1 dBm","-2 dBm","-3 dBm","-4 dBm","-5 dBm","-6 dBm","-7 dBm","-8 dBm","-9 dBm"],	description: "Signalstärke angeben"]]
        command "setAssociationGroup", [[name: "Group Number*",			type:"NUMBER", 	description: "Provide the association group number to edit"], 
                                        [name: "Z-Wave Node*", 			type:"NUMBER", 	description: "Enter the node number (Integer) associated with the node"], 
                                        [name: "Action*", 				type:"ENUM", 	constraints: ["Add", "Remove"]]]        
        
		fingerprint deviceId:"1", inClusters: "0x5E,0x6C,0x55,0x98,0x9F", outClusters: "", mfr:"0328", prod:"0005", model:"0001" 
		}	
	
    def tempReportRates = [:]
		tempReportRates << ["0"  : "Temperatur nicht automatisch senden"] 								//0x00
		tempReportRates << ["1"  : "Temperatur senden bei Differenz von 0.1°C"]							//0x01
		tempReportRates << ["2"  : "Temperatur senden bei Differenz von 0.2°C"]							//0x02
		tempReportRates << ["3"  : "Temperatur senden bei Differenz von 0.3°C"] 						//0x03
		tempReportRates << ["4"  : "Temperatur senden bei Differenz von 0.4°C"] 						//0x04
		tempReportRates << ["5"  : "Temperatur senden bei Differenz von 0.5°C"] 						//0x05
		tempReportRates << ["6"  : "Temperatur senden bei Differenz von 0.6°C"] 						//0x06
		tempReportRates << ["7"  : "Temperatur senden bei Differenz von 0.7°C"] 						//0x07
		tempReportRates << ["8"  : "Temperatur senden bei Differenz von 0.8°C"] 						//0x08
		tempReportRates << ["9"  : "Temperatur senden bei Differenz von 0.9°C"] 						//0x09
		tempReportRates << ["10" : "Temperatur senden bei Differenz von 1.0°C"] 						//0x0A
		tempReportRates << ["11" : "Temperatur senden bei Differenz von 1.1°C"] 						//0x0B
		tempReportRates << ["12" : "Temperatur senden bei Differenz von 1.2°C"] 						//0x0C
		tempReportRates << ["13" : "Temperatur senden bei Differenz von 1.3°C"] 						//0x0D
		tempReportRates << ["14" : "Temperatur senden bei Differenz von 1.4°C"] 						//0x0E
		tempReportRates << ["15" : "Temperatur senden bei Differenz von 1.5°C"] 						//0x0F
		tempReportRates << ["16" : "Temperatur senden bei Differenz von 1.6°C"] 						//0x10
		tempReportRates << ["17" : "Temperatur senden bei Differenz von 1.7°C"] 						//0x11
		tempReportRates << ["18" : "Temperatur senden bei Differenz von 1.8°C"] 						//0x12
		tempReportRates << ["19" : "Temperatur senden bei Differenz von 1.9°C"] 						//0x13
		tempReportRates << ["20" : "Temperatur senden bei Differenz von 2.0°C"] 						//0x14
		tempReportRates << ["21" : "Temperatur senden bei Differenz von 2.1°C"] 						//0x15
		tempReportRates << ["22" : "Temperatur senden bei Differenz von 2.2°C"] 						//0x16
		tempReportRates << ["23" : "Temperatur senden bei Differenz von 2.3°C"] 						//0x17
		tempReportRates << ["24" : "Temperatur senden bei Differenz von 2.4°C"] 						//0x18
		tempReportRates << ["25" : "Temperatur senden bei Differenz von 2.5°C"] 						//0x19
		tempReportRates << ["26" : "Temperatur senden bei Differenz von 2.6°C"] 						//0x1A
		tempReportRates << ["27" : "Temperatur senden bei Differenz von 2.7°C"] 						//0x1B
		tempReportRates << ["28" : "Temperatur senden bei Differenz von 2.8°C"] 						//0x1C
		tempReportRates << ["29" : "Temperatur senden bei Differenz von 2.9°C"] 						//0x1D
		tempReportRates << ["30" : "Temperatur senden bei Differenz von 3.0°C"] 						//0x1E
		tempReportRates << ["31" : "Temperatur senden bei Differenz von 3.1°C"] 						//0x1F
		tempReportRates << ["32" : "Temperatur senden bei Differenz von 3.2°C"] 						//0x20
		tempReportRates << ["33" : "Temperatur senden bei Differenz von 3.3°C"] 						//0x21
		tempReportRates << ["34" : "Temperatur senden bei Differenz von 3.4°C"] 						//0x22
		tempReportRates << ["35" : "Temperatur senden bei Differenz von 3.5°C"] 						//0x23
		tempReportRates << ["36" : "Temperatur senden bei Differenz von 3.6°C"] 						//0x24
		tempReportRates << ["37" : "Temperatur senden bei Differenz von 3.7°C"] 						//0x25
		tempReportRates << ["38" : "Temperatur senden bei Differenz von 3.8°C"] 						//0x26
		tempReportRates << ["39" : "Temperatur senden bei Differenz von 3.9°C"] 						//0x27
		tempReportRates << ["40" : "Temperatur senden bei Differenz von 4.0°C"] 						//0x28
		tempReportRates << ["41" : "Temperatur senden bei Differenz von 4.1°C"] 						//0x29
		tempReportRates << ["42" : "Temperatur senden bei Differenz von 4.2°C"] 						//0x2A
		tempReportRates << ["43" : "Temperatur senden bei Differenz von 4.3°C"] 						//0x2B
		tempReportRates << ["44" : "Temperatur senden bei Differenz von 4.4°C"] 						//0x2C
		tempReportRates << ["45" : "Temperatur senden bei Differenz von 4.5°C"] 						//0x2D
		tempReportRates << ["46" : "Temperatur senden bei Differenz von 4.6°C"] 						//0x2E
		tempReportRates << ["47" : "Temperatur senden bei Differenz von 4.7°C"] 						//0x2F
		tempReportRates << ["48" : "Temperatur senden bei Differenz von 4.8°C"] 						//0x30
		tempReportRates << ["49" : "Temperatur senden bei Differenz von 4.9°C"] 						//0x31
		tempReportRates << ["50" : "Temperatur senden bei Differenz von 5.0°C"] 						//0x32

    def humReportRates = [:]
        humReportRates << ["0"  : "Feuchtigkeit bei Änderung nicht melden"]                         //0x00
        humReportRates << ["1"  : "Feuchtigkeit senden bei Differenz von 1%"]     					//0x01
        humReportRates << ["2"  : "Feuchtigkeit senden bei Differenz von 2%"]     					//0x02
        humReportRates << ["3"  : "Feuchtigkeit senden bei Differenz von 3%"]     					//0x03
        humReportRates << ["4"  : "Feuchtigkeit senden bei Differenz von 4%"]    					//0x04
        humReportRates << ["5"  : "Feuchtigkeit senden bei Differenz von 5%"]     					//0x05
        humReportRates << ["6"  : "Feuchtigkeit senden bei Differenz von 6%"]     					//0x06
        humReportRates << ["7"  : "Feuchtigkeit senden bei Differenz von 7%"]     					//0x07
        humReportRates << ["8"  : "Feuchtigkeit senden bei Differenz von 8%"]     					//0x08
        humReportRates << ["9"  : "Feuchtigkeit senden bei Differenz von 9%"]     					//0x09
        humReportRates << ["10" : "Feuchtigkeit senden bei Differenz von 10%"]     					//0x0A	
		humReportRates << ["11" : "Feuchtigkeit senden bei Differenz von 11%"] 						//0x0B
		humReportRates << ["12" : "Feuchtigkeit senden bei Differenz von 12%"] 						//0x0C
		humReportRates << ["13" : "Feuchtigkeit senden bei Differenz von 13%"] 						//0x0D
		humReportRates << ["14" : "Feuchtigkeit senden bei Differenz von 14%"] 						//0x0E
		humReportRates << ["15" : "Feuchtigkeit senden bei Differenz von 15%"] 						//0x0F
		humReportRates << ["16" : "Feuchtigkeit senden bei Differenz von 16%"] 						//0x10
		humReportRates << ["17" : "Feuchtigkeit senden bei Differenz von 17%"] 						//0x11
		humReportRates << ["18" : "Feuchtigkeit senden bei Differenz von 18%"] 						//0x12
		humReportRates << ["19" : "Feuchtigkeit senden bei Differenz von 19%"] 						//0x13
		humReportRates << ["20" : "Feuchtigkeit senden bei Differenz von 20%"] 						//0x14
		humReportRates << ["21" : "Feuchtigkeit senden bei Differenz von 21%"] 						//0x15
		humReportRates << ["22" : "Feuchtigkeit senden bei Differenz von 22%"] 						//0x16
		humReportRates << ["23" : "Feuchtigkeit senden bei Differenz von 23%"] 						//0x17
		humReportRates << ["24" : "Feuchtigkeit senden bei Differenz von 24%"] 						//0x18
		humReportRates << ["25" : "Feuchtigkeit senden bei Differenz von 25%"] 						//0x19
		humReportRates << ["26" : "Feuchtigkeit senden bei Differenz von 26%"] 						//0x1A
		humReportRates << ["27" : "Feuchtigkeit senden bei Differenz von 27%"] 						//0x1B
		humReportRates << ["28" : "Feuchtigkeit senden bei Differenz von 28%"] 						//0x1C
		humReportRates << ["29" : "Feuchtigkeit senden bei Differenz von 29%"] 						//0x1D
		humReportRates << ["30" : "Feuchtigkeit senden bei Differenz von 30%"] 						//0x1E
		humReportRates << ["31" : "Feuchtigkeit senden bei Differenz von 31%"] 						//0x1F
		humReportRates << ["32" : "Feuchtigkeit senden bei Differenz von 32%"] 						//0x20
		humReportRates << ["33" : "Feuchtigkeit senden bei Differenz von 33%"] 						//0x21
		humReportRates << ["34" : "Feuchtigkeit senden bei Differenz von 34%"] 						//0x22
		humReportRates << ["35" : "Feuchtigkeit senden bei Differenz von 35%"] 						//0x23
		humReportRates << ["36" : "Feuchtigkeit senden bei Differenz von 36%"] 						//0x24
		humReportRates << ["37" : "Feuchtigkeit senden bei Differenz von 37%"] 						//0x25
		humReportRates << ["38" : "Feuchtigkeit senden bei Differenz von 38%"] 						//0x26
		humReportRates << ["39" : "Feuchtigkeit senden bei Differenz von 39%"] 						//0x27
		humReportRates << ["40" : "Feuchtigkeit senden bei Differenz von 40%"] 						//0x28
		humReportRates << ["41" : "Feuchtigkeit senden bei Differenz von 41%"] 						//0x29
		humReportRates << ["42" : "Feuchtigkeit senden bei Differenz von 42%"] 						//0x2A
		humReportRates << ["43" : "Feuchtigkeit senden bei Differenz von 43%"] 						//0x2B
		humReportRates << ["44" : "Feuchtigkeit senden bei Differenz von 44%"] 						//0x2C
		humReportRates << ["45" : "Feuchtigkeit senden bei Differenz von 45%"] 						//0x2D
		humReportRates << ["46" : "Feuchtigkeit senden bei Differenz von 46%"] 						//0x2E
		humReportRates << ["47" : "Feuchtigkeit senden bei Differenz von 47%"] 						//0x2F
		humReportRates << ["48" : "Feuchtigkeit senden bei Differenz von 48%"] 						//0x30
		humReportRates << ["49" : "Feuchtigkeit senden bei Differenz von 49%"] 						//0x31
		humReportRates << ["50" : "Feuchtigkeit senden bei Differenz von 50%"] 						//0x32

    def tempEinheit = [:]
        tempEinheit << ["0":"Celcius"]          //0x00
        tempEinheit << ["1":"Fahrenheit"]       //0x01

    def tempAufloesung = [:]
        tempAufloesung << ["0":"Keine Nachkommastelle"] //0x00
        tempAufloesung << ["1":"Eine Nachkommastelle"]  //0x01
        tempAufloesung << ["2":"Zwei Nachkommastellen"] //0x02

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
        co2ChangeReport << ["0":"Keine Meldung"]                     //0x00
        co2ChangeReport << ["1":"Meldung bei Änderung um 100 ppm"]   //0x01
        co2ChangeReport << ["2":"Meldung bei Änderung um 200 ppm"]   //0x02
        co2ChangeReport << ["3":"Meldung bei Änderung um 300 ppm"]   //0x03
        co2ChangeReport << ["4":"Meldung bei Änderung um 400 ppm"]   //0x04
        co2ChangeReport << ["5":"Meldung bei Änderung um 500 ppm"]   //0x05
        co2ChangeReport << ["6":"Meldung bei Änderung um 600 ppm"]   //0x06
        co2ChangeReport << ["7":"Meldung bei Änderung um 700 ppm"]   //0x07
        co2ChangeReport << ["8":"Meldung bei Änderung um 800 ppm"]   //0x08
        co2ChangeReport << ["9":"Meldung bei Änderung um 900 ppm"]   //0x09
        co2ChangeReport << ["10":"Meldung bei Änderung um 1000 ppm"] //0x0A

    def ledIndikator = [:]
        ledIndikator << ["0":"Led aus"] //0x00
        ledIndikator << ["1":"Led ein"] //0x01

	preferences {
        input name: "Temperaturdifferenz",		type: "enum", title: "Temperatur on Change Reporting", 	options: tempReportRates,	description: "Default: 0.5°C",							defaultValue:"5",	required: false
        input name: "Feuchtigkeitsdifferenz",	type: "enum", title: "Feuchtigkeit on Change Reporting",options: humReportRates,	description: "Default: 5%",								defaultValue:"5",	required: false
        input name: "Temperatureeinheit",		type: "enum", title: "Temperatureeinheit", 				options: tempEinheit, 		description: "Default: °C",								defaultValue:"0",	required: false
        input name: "TemperaturAufloesung",		type: "enum", title: "Auflösung Temperatur", 			options: tempAufloesung, 	description: "Default: Eine Nachkommastelle", 			defaultValue:"1",	required: false
        input name: "FeuchtigkeitsAufloesung",	type: "enum", title: "Auflösung Feuchte", 				options: humAufloesung, 	description: "Default: Keine Nachkommastelle", 			defaultValue:"0",	required: false
        input name: "VOCChageReporting",		type: "enum", title: "VOC-on Change Reporting", 		options: vocChangeReport, 	description: "Default: 0.5 ppm", 						defaultValue:"5",	required: false
        input name: "CO2ChangeReporting",		type: "enum", title: "CO2 Change Reporting", 			options: co2ChangeReport, 	description: "Default: 500 ppm", 						defaultValue:"5",	required: false
        input name: "LedIndikation",			type: "enum", title: "Luftgüte per LED signalisieren", 	options: ledIndikator, 		description: "Default: Luftgüte per Led signalisieren", defaultValue:"1",	required: false   
	}

}

def installed() {    
    sendEvent(name: "tamper", value: "clear", displayed: false)
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
	sendHubCommand(new hubitat.device.HubAction(new hubitat.zwave.commands.zwaveplusinfov2.ZwaveplusInfoGet().format(), hubitat.device.Protocol.ZWAVE))
	sendHubCommand(new hubitat.device.HubAction(new hubitat.zwave.commands.firmwareupdatemdv3.FirmwareMdGet().format(), hubitat.device.Protocol.ZWAVE))
	response([
		secureSequence(zwave.versionV1.versionGet()),     
		"delay 500",
		secureSequence(zwave.powerlevelV1.powerlevelGet()),     
		"delay 500",
		secureSequence(zwave.notificationV8.notificationGet(notificationType:13, event:6)),     
		"delay 500",
		secureSequence(zwave.manufacturerSpecificV2.manufacturerSpecificGet()),     	
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:1)),     
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:2)),     
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:3)),     
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:4)),     
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:5)),     
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:6)),     
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:7)),     
		"delay 500",
		secureSequence(zwave.configurationV1.configurationGet(parameterNumber:8)),     
		"delay 500",
		secureSequence(zwave.associationV2.associationGet(groupingIdentifier:1)),     
		"delay 500",
		secureSequence(zwave.associationV2.associationGet(groupingIdentifier:2)),     
		"delay 500",
		secureSequence(zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType: 0x01, scale: tmpE)),     // temperature
		"delay 500",
        secureSequence(zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType: 0x05, scale: 0)),        // humidity
		"delay 500",
		secureSequence(zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType: 0x0B, scale: tmpE)),     // dewpoint
		"delay 500",
        secureSequence(zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType: 0x11, scale: 0)),        // CO2
		"delay 500",
        secureSequence(zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType: 0x27, scale: 1)),        // VOC
		"delay 500"
	])     
}

def parse(String description) {
	def results = []
	if (description.startsWith("Err")) {
		results += createEvent(descriptionText: description, displayed: true)
	} else {
		def cmd = zwave.parse(description,[0x85:2,0x59:1,0x70:1,0x5A:1,0x7A:1,0x72:1,0x31:10,0x71:8,0x73:1,0x98:2,0x6C:1,0x55:2,0x86:2,0x0A:2]) //0x98 и 0x55 задокументирован только для версии 1 у хубитат
		if (cmd) {
			results += zwaveEvent(cmd)
		}
	}
    return results
}

void updated() {
	configure()
}

//Association V2
//Command Class: 0x85
	//Association Report
	//Command: 0x03
	def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
    	log.debug "${device.label?device.label:device.name}: ${cmd}"
    	def temp = []
    	if (cmd.nodeId != []) {
       		cmd.nodeId.each {
          		temp += it.toInteger()
       		}
    	} 
    	state."actualAssociation${cmd.groupingIdentifier}" = temp
    	log.info "${device.label?device.label:device.name}: Associations for Group ${cmd.groupingIdentifier}: ${temp}"
    	updateDataValue("associationGroup${cmd.groupingIdentifier}", "$temp")
	}

	//Association Groupings Report
	//Command: 0x06
	def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
    	log.debug "${device.label?device.label:device.name}: ${cmd}"
    	sendEvent(name: "groups", value: cmd.supportedGroupings)
    	log.info "${device.label?device.label:device.name}: Supported association groups: ${cmd.supportedGroupings}"
    	state.associationGroups = cmd.supportedGroupings
	}


//Association Grp Info V1
//Command Class: 0x59
	//Command: 0x02
	def zwaveEvent(hubitat.zwave.commands.associationgrpinfov1.AssociationGroupNameReport cmd) {
	     //Short groupingIdentifier
	     //Short lengthOfName
	     //List<AssociationGroupNameReport> name
		log.info "AssociationGroupNameReport : $cmd"
	}

//Configuration V1
//Command Class: 0x70
	//Configuration Report
	//Command: 0x06
	def zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {
		//List<ConfigurationReport> configurationValue
		//Short parameterNumber
		//BigInteger scaledConfigurationValue
		//Short size
		def events = []
		def cnf = ""
		if (device.currentValue("Configuration")?.startsWith("sent")) { 
			cnf = "receive:"			
		} else {
			cnf = device.currentValue("Configuration")
		}
		switch (cmd.parameterNumber) {
			case 1:
				def tempReportRates = [:]
					tempReportRates << ["0"  : "Temperatur nicht automatisch senden"      ] 						//0x00
					tempReportRates << ["1"  : "Temperatur senden bei Differenz von 0.1°C"]							//0x01
					tempReportRates << ["2"  : "Temperatur senden bei Differenz von 0.2°C"]							//0x02
					tempReportRates << ["3"  : "Temperatur senden bei Differenz von 0.3°C"] 						//0x03
					tempReportRates << ["4"  : "Temperatur senden bei Differenz von 0.4°C"] 						//0x04
					tempReportRates << ["5"  : "Temperatur senden bei Differenz von 0.5°C"] 						//0x05
					tempReportRates << ["6"  : "Temperatur senden bei Differenz von 0.6°C"] 						//0x06
					tempReportRates << ["7"  : "Temperatur senden bei Differenz von 0.7°C"] 						//0x07
					tempReportRates << ["8"  : "Temperatur senden bei Differenz von 0.8°C"] 						//0x08
					tempReportRates << ["9"  : "Temperatur senden bei Differenz von 0.9°C"] 						//0x09
					tempReportRates << ["10" : "Temperatur senden bei Differenz von 1.0°C"] 						//0x0A
					tempReportRates << ["11" : "Temperatur senden bei Differenz von 1.1°C"] 						//0x0B
					tempReportRates << ["12" : "Temperatur senden bei Differenz von 1.2°C"] 						//0x0C
					tempReportRates << ["13" : "Temperatur senden bei Differenz von 1.3°C"] 						//0x0D
					tempReportRates << ["14" : "Temperatur senden bei Differenz von 1.4°C"] 						//0x0E
					tempReportRates << ["15" : "Temperatur senden bei Differenz von 1.5°C"] 						//0x0F
					tempReportRates << ["16" : "Temperatur senden bei Differenz von 1.6°C"] 						//0x10
					tempReportRates << ["17" : "Temperatur senden bei Differenz von 1.7°C"] 						//0x11
					tempReportRates << ["18" : "Temperatur senden bei Differenz von 1.8°C"] 						//0x12
					tempReportRates << ["19" : "Temperatur senden bei Differenz von 1.9°C"] 						//0x13
					tempReportRates << ["20" : "Temperatur senden bei Differenz von 2.0°C"] 						//0x14
					tempReportRates << ["21" : "Temperatur senden bei Differenz von 2.1°C"] 						//0x15
					tempReportRates << ["22" : "Temperatur senden bei Differenz von 2.2°C"] 						//0x16
					tempReportRates << ["23" : "Temperatur senden bei Differenz von 2.3°C"] 						//0x17
					tempReportRates << ["24" : "Temperatur senden bei Differenz von 2.4°C"] 						//0x18
					tempReportRates << ["25" : "Temperatur senden bei Differenz von 2.5°C"] 						//0x19
					tempReportRates << ["26" : "Temperatur senden bei Differenz von 2.6°C"] 						//0x1A
					tempReportRates << ["27" : "Temperatur senden bei Differenz von 2.7°C"] 						//0x1B
					tempReportRates << ["28" : "Temperatur senden bei Differenz von 2.8°C"] 						//0x1C
					tempReportRates << ["29" : "Temperatur senden bei Differenz von 2.9°C"] 						//0x1D
					tempReportRates << ["30" : "Temperatur senden bei Differenz von 3.0°C"] 						//0x1E
					tempReportRates << ["31" : "Temperatur senden bei Differenz von 3.1°C"] 						//0x1F
					tempReportRates << ["32" : "Temperatur senden bei Differenz von 3.2°C"] 						//0x20
					tempReportRates << ["33" : "Temperatur senden bei Differenz von 3.3°C"] 						//0x21
					tempReportRates << ["34" : "Temperatur senden bei Differenz von 3.4°C"] 						//0x22
					tempReportRates << ["35" : "Temperatur senden bei Differenz von 3.5°C"] 						//0x23
					tempReportRates << ["36" : "Temperatur senden bei Differenz von 3.6°C"] 						//0x24
					tempReportRates << ["37" : "Temperatur senden bei Differenz von 3.7°C"] 						//0x25
					tempReportRates << ["38" : "Temperatur senden bei Differenz von 3.8°C"] 						//0x26
					tempReportRates << ["39" : "Temperatur senden bei Differenz von 3.9°C"] 						//0x27
					tempReportRates << ["40" : "Temperatur senden bei Differenz von 4.0°C"] 						//0x28
					tempReportRates << ["41" : "Temperatur senden bei Differenz von 4.1°C"] 						//0x29
					tempReportRates << ["42" : "Temperatur senden bei Differenz von 4.2°C"] 						//0x2A
					tempReportRates << ["43" : "Temperatur senden bei Differenz von 4.3°C"] 						//0x2B
					tempReportRates << ["44" : "Temperatur senden bei Differenz von 4.4°C"] 						//0x2C
					tempReportRates << ["45" : "Temperatur senden bei Differenz von 4.5°C"] 						//0x2D
					tempReportRates << ["46" : "Temperatur senden bei Differenz von 4.6°C"] 						//0x2E
					tempReportRates << ["47" : "Temperatur senden bei Differenz von 4.7°C"] 						//0x2F
					tempReportRates << ["48" : "Temperatur senden bei Differenz von 4.8°C"] 						//0x30
					tempReportRates << ["49" : "Temperatur senden bei Differenz von 4.9°C"] 						//0x31
					tempReportRates << ["50" : "Temperatur senden bei Differenz von 5.0°C"] 						//0x32
				def msg = cmd.scaledConfigurationValue.toString()				
				log.info "Temperaturreport eingestellt auf ${tempReportRates[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 1"), displayed: true)
				break;			
			case 2:
				def humReportRates = [:]
					humReportRates << ["0"  : "Feuchtigkeit bei Änderung nicht melden"]                         //0x00
					humReportRates << ["1"  : "Feuchtigkeit senden bei Differenz von 1%" ]     					//0x01
					humReportRates << ["2"  : "Feuchtigkeit senden bei Differenz von 2%" ]     					//0x02
					humReportRates << ["3"  : "Feuchtigkeit senden bei Differenz von 3%" ]     					//0x03
					humReportRates << ["4"  : "Feuchtigkeit senden bei Differenz von 4%" ]    					//0x04
					humReportRates << ["5"  : "Feuchtigkeit senden bei Differenz von 5%" ]     					//0x05
					humReportRates << ["6"  : "Feuchtigkeit senden bei Differenz von 6%" ]     					//0x06
					humReportRates << ["7"  : "Feuchtigkeit senden bei Differenz von 7%" ]     					//0x07
					humReportRates << ["8"  : "Feuchtigkeit senden bei Differenz von 8%" ]     					//0x08
					humReportRates << ["9"  : "Feuchtigkeit senden bei Differenz von 9%" ]     					//0x09
					humReportRates << ["10" : "Feuchtigkeit senden bei Differenz von 10%"]     					//0x0A	
					humReportRates << ["11" : "Feuchtigkeit senden bei Differenz von 11%"] 						//0x0B
					humReportRates << ["12" : "Feuchtigkeit senden bei Differenz von 12%"] 						//0x0C
					humReportRates << ["13" : "Feuchtigkeit senden bei Differenz von 13%"] 						//0x0D
					humReportRates << ["14" : "Feuchtigkeit senden bei Differenz von 14%"] 						//0x0E
					humReportRates << ["15" : "Feuchtigkeit senden bei Differenz von 15%"] 						//0x0F
					humReportRates << ["16" : "Feuchtigkeit senden bei Differenz von 16%"] 						//0x10
					humReportRates << ["17" : "Feuchtigkeit senden bei Differenz von 17%"] 						//0x11
					humReportRates << ["18" : "Feuchtigkeit senden bei Differenz von 18%"] 						//0x12
					humReportRates << ["19" : "Feuchtigkeit senden bei Differenz von 19%"] 						//0x13
					humReportRates << ["20" : "Feuchtigkeit senden bei Differenz von 20%"] 						//0x14
					humReportRates << ["21" : "Feuchtigkeit senden bei Differenz von 21%"] 						//0x15
					humReportRates << ["22" : "Feuchtigkeit senden bei Differenz von 22%"] 						//0x16
					humReportRates << ["23" : "Feuchtigkeit senden bei Differenz von 23%"] 						//0x17
					humReportRates << ["24" : "Feuchtigkeit senden bei Differenz von 24%"] 						//0x18
					humReportRates << ["25" : "Feuchtigkeit senden bei Differenz von 25%"] 						//0x19
					humReportRates << ["26" : "Feuchtigkeit senden bei Differenz von 26%"] 						//0x1A
					humReportRates << ["27" : "Feuchtigkeit senden bei Differenz von 27%"] 						//0x1B
					humReportRates << ["28" : "Feuchtigkeit senden bei Differenz von 28%"] 						//0x1C
					humReportRates << ["29" : "Feuchtigkeit senden bei Differenz von 29%"] 						//0x1D
					humReportRates << ["30" : "Feuchtigkeit senden bei Differenz von 30%"] 						//0x1E
					humReportRates << ["31" : "Feuchtigkeit senden bei Differenz von 31%"] 						//0x1F
					humReportRates << ["32" : "Feuchtigkeit senden bei Differenz von 32%"] 						//0x20
					humReportRates << ["33" : "Feuchtigkeit senden bei Differenz von 33%"] 						//0x21
					humReportRates << ["34" : "Feuchtigkeit senden bei Differenz von 34%"] 						//0x22
					humReportRates << ["35" : "Feuchtigkeit senden bei Differenz von 35%"] 						//0x23
					humReportRates << ["36" : "Feuchtigkeit senden bei Differenz von 36%"] 						//0x24
					humReportRates << ["37" : "Feuchtigkeit senden bei Differenz von 37%"] 						//0x25
					humReportRates << ["38" : "Feuchtigkeit senden bei Differenz von 38%"] 						//0x26
					humReportRates << ["39" : "Feuchtigkeit senden bei Differenz von 39%"] 						//0x27
					humReportRates << ["40" : "Feuchtigkeit senden bei Differenz von 40%"] 						//0x28
					humReportRates << ["41" : "Feuchtigkeit senden bei Differenz von 41%"] 						//0x29
					humReportRates << ["42" : "Feuchtigkeit senden bei Differenz von 42%"] 						//0x2A
					humReportRates << ["43" : "Feuchtigkeit senden bei Differenz von 43%"] 						//0x2B
					humReportRates << ["44" : "Feuchtigkeit senden bei Differenz von 44%"] 						//0x2C
					humReportRates << ["45" : "Feuchtigkeit senden bei Differenz von 45%"] 						//0x2D
					humReportRates << ["46" : "Feuchtigkeit senden bei Differenz von 46%"] 						//0x2E
					humReportRates << ["47" : "Feuchtigkeit senden bei Differenz von 47%"] 						//0x2F
					humReportRates << ["48" : "Feuchtigkeit senden bei Differenz von 48%"] 						//0x30
					humReportRates << ["49" : "Feuchtigkeit senden bei Differenz von 49%"] 						//0x31
					humReportRates << ["50" : "Feuchtigkeit senden bei Differenz von 50%"] 						//0x32
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "Feuchtigkeitsreport eingestellt auf ${humReportRates[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 2"), displayed: true)
				break;				
			case 3:
				def tempEinheit = [:]
					tempEinheit << ["0":"Celcius"   ]
					tempEinheit << ["1":"Fahrenheit"]
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "Temperatureinheit ist ${tempEinheit[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 3"), displayed: true)
				break;				
			case 4:
				def tempAufloesung = [:]
					tempAufloesung << ["0":"Keine Nachkommastelle"] 
					tempAufloesung << ["1":"Eine Nachkommastelle" ]  
					tempAufloesung << ["2":"Zwei Nachkommastellen"] 
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "Temperaturauflösung ist ${tempAufloesung[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 4"), displayed: true)
				break;				
			case 5:
				def humAufloesung = [:]
					humAufloesung << ["0":"Keine Nachkommastelle"]  
					humAufloesung << ["1":"Eine Nachkommastelle" ]   
					humAufloesung << ["2":"Zwei Nachkommastellen"]  
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "Feuchtigkeitsauflösung ist ${humAufloesung[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 5"), displayed: true)
				break;				
			case 6:
				def vocChangeReport = [:]
					vocChangeReport << ["0":"Keine Meldung"                   ]                   
					vocChangeReport << ["1":"Meldung bei Änderung um 0.1 ppm" ]  
					vocChangeReport << ["2":"Meldung bei Änderung um 0.2 ppm" ]  
					vocChangeReport << ["3":"Meldung bei Änderung um 0.3 ppm" ]
					vocChangeReport << ["4":"Meldung bei Änderung um 0.4 ppm" ]  
					vocChangeReport << ["5":"Meldung bei Änderung um 0.5 ppm" ]  
					vocChangeReport << ["6":"Meldung bei Änderung um 0.6 ppm" ]  
					vocChangeReport << ["7":"Meldung bei Änderung um 0.7 ppm" ]  
					vocChangeReport << ["8":"Meldung bei Änderung um 0.8 ppm" ]  
					vocChangeReport << ["9":"Meldung bei Änderung um 0.9 ppm" ]  
					vocChangeReport << ["10":"Meldung bei Änderung um 1.0 ppm"]
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "VOC-Report ist ${vocChangeReport[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 6"), displayed: true)
				break;				
			case 7:
				def co2ChangeReport = [:]
					co2ChangeReport << ["0":"Keine Meldung"                    ]                     
					co2ChangeReport << ["1":"Meldung bei Änderung um 100 ppm"  ]   
					co2ChangeReport << ["2":"Meldung bei Änderung um 200 ppm"  ]   
					co2ChangeReport << ["3":"Meldung bei Änderung um 300 ppm"  ]   
					co2ChangeReport << ["4":"Meldung bei Änderung um 400 ppm"  ]   
					co2ChangeReport << ["5":"Meldung bei Änderung um 500 ppm"  ]   
					co2ChangeReport << ["6":"Meldung bei Änderung um 600 ppm"  ]   
					co2ChangeReport << ["7":"Meldung bei Änderung um 700 ppm"  ]   
					co2ChangeReport << ["8":"Meldung bei Änderung um 800 ppm"  ]   
					co2ChangeReport << ["9":"Meldung bei Änderung um 900 ppm"  ]   
					co2ChangeReport << ["10":"Meldung bei Änderung um 1000 ppm"] 
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "CO2-Report ist ${co2ChangeReport[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 7"), displayed: true)
				break;				
			case 8:
				def ledIndikator = [:]
					ledIndikator << ["0":"Led aus"]
					ledIndikator << ["1":"Led ein"]
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "${ledIndikator[msg]}"
				sendEvent(name: "Configuration", value: (cnf + " 8"), displayed: true)
				break;
		}
	}

//Device Reset Locally V1
//Command Class: 0x5A
	//Device Reset Locally Notification
	//Command: 0x01
	def zwaveEvent(hubitat.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
		createEvent(name:"tamper", value: "detected", displayed: true)
	}

//Manufacturer Specific V1
//Command Class: 0x72
	//Manufacturer Specific Report
	//Command: 0x05
	def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv1.ManufacturerSpecificReport cmd) {
		//Integer manufacturerId
		//String manufacturerName
		//Integer productId
		//Integer productTypeId
		if (cmd.manufacturerId)		{ updateDataValue("manufacturerId",	cmd.manufacturerId.toString()) }
		if (cmd.manufacturerName)	{ updateDataValue("manufacturer",	cmd.manufacturerName) }
		if (cmd.productId)			{ updateDataValue("productId",		cmd.productId.toString()) }
		if (cmd.productTypeId)		{ updateDataValue("productTypeId",	cmd.productTypeId.toString()) }		
	}

//Firmware Update Md V3
//Command Class: 0x7A
	//Firmware Md Report
	//Command: 0x02
	def zwaveEvent (hubitat.zwave.commands.firmwareupdatemdv3.FirmwareMdReport cmd) {
		//Integer checksum
		//Integer firmwareId
		//List<FirmwareMdReport> firmwareIds
		//Boolean firmwareUpgradable
		//Integer manufacturerId
		//Integer maxFragmentSize
		//Short numberOfTargets
 		log.info "$cmd"
 }

	//Firmware Update Md Status Report
	//Command: 0x07
	def zwaveEvent (hubitat.zwave.commands.firmwareupdatemdv3.FirmwareUpdateMdStatusReport cmd) {
		//Short status
		//static Short STATUS_IINVALID_FRAGMENT_SIZE = 2
		//static Short STATUS_NOT_DOWNLOADABLE = 3
		//static Short STATUS_SUCCESSFULLY = 255
		//static Short STATUS_UNABLE_TO_RECEIVE = 1
		//static Short STATUS_UNABLE_TO_RECEIVE_WITHOUT_CHECKSUM_ERROR = 0
 		log.info "$cmd"
 }

//Sensor Multilevel V10
//Command Class: 0x31
	//Sensor Multilevel Report
	//Command: 0x05
	def zwaveEvent (hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelReport cmd) {
		//Short precision
		//Short scale
		//BigDecimal scaledSensorValue
		//Short sensorType
		//Short size	
		//static Short SENSOR_TYPE_CO2_LEVEL_VERSION_3 = 17
		//static Short SENSOR_TYPE_DEW_POINT_VERSION_2 = 11
		//static Short SENSOR_TYPE_RELATIVE_HUMIDITY_VERSION_2 = 5
		//static Short SENSOR_TYPE_TEMPERATURE_VERSION_1 = 1
		//static Short SENSOR_TYPE_VOLATILE_ORGANIC_COMPOUND_V7 = 39
		def map = [:]
		def rund = 0
		def result = []
		def msg = ""
		switch (cmd.sensorType) {
			case 0x01:
				map.name = "temperature"
				map.unit = cmd.scale == 1 ? "°F" : "°C"
				map.value = cmd.scaledSensorValue.toString()
				log.info "Temperature ist ${map.value}"
				state.actualAssociation2.each {
					sendHubCommand(new hubitat.device.HubAction(new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelReport(sensorType:1,scale: cmd.scale, precision:cmd.precision, scaledSensorValue: cmd.scaledSensorValue, size:cmd.size).format(), hubitat.device.Protocol.ZWAVE,it.toString()))
					def c = new hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelReport(sensorType:1,scale: cmd.scale, precision:cmd.precision, scaledSensorValue: cmd.scaledSensorValue, size:cmd.size).format()
					log.info "$c"
				}
				break;
			case 0x05:
				map.name = "humidity"			
				map.unit = cmd.scale == 1 ? "g/m^3" : "%"
				map.value = cmd.scaledSensorValue.toString()
				log.info "Feuchtigkeit ist ${map.value}"
				break;
			case 0x0B:
				map.name = "DewPoint"
				map.unit = cmd.scale == 1 ? "°F" : "°C"
				map.value = cmd.scaledSensorValue.toString()
				log.info "Dew-Point ist ${map.value}"
				break;
			case 0x11:
				map.name = "carbonDioxide"
				map.unit = "ppm"
				map.value = cmd.scaledSensorValue.toInteger()
				log.info "CO2 ist ${map.value}"
				if (map.value < 800) {
					co2Notifity (1)
					} else if (map.value < 1000) {
						co2Notifity (2)
						} else if (map.value < 1400) {
							co2Notifity (3)
							} else if (map.value < 2000) {
								co2Notifity (4)
								} else {
									co2Notifity (5)
									}
				break;
			case 0x27:
				map.name = "VOC"
				map.unit = "ppb"
				map.value = (cmd.scaledSensorValue.toFloat() * 1000).toInteger()
				log.info "VOC ist ${map.value}"
				if (map.value < 65) {
					vocNotifity (0)
					} else if (map.value < 220) {
						vocNotifity (1)
						} else if (map.value < 660) {
							vocNotifity (2)
							} else if (map.value < 2200) {
								vocNotifity (3)
								} else if (map.value < 5500) {
									vocNotifity (4)
									} else { 
										vocNotifity (5)
										}           
				break;
			default:
				map.descriptionText = cmd.toString()
		}
		createEvent(map)
	}

//Notification V8
//Command Class: 0x71
	//Notification Report
	//Command: 0x05
	def zwaveEvent(hubitat.zwave.commands.notificationv8.NotificationReport cmd) {
		//Short event
		//Short eventParametersLength
		//Short notificationStatus
		//Short notificationType
		//Short reserved
		//Boolean sequence
		//Short v1AlarmLevel
		//Short v1AlarmType
		//static Short NOTIFICATION_TYPE_HOME_HEALTH = 13
		log.info "Home-Health hat Niveau ${cmd.eventParameter[0]} gemeldet"
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
					log.warn "Warn!!! Home-Health liefert wieder falsche Werte!!! Hubitatfehler!!!"
				}
			} else {
				log.warn "Warnung!!! Home-Health hat falsche Event gemeldet!!!"
			}
		} else {
			log.warn "Warnung!!! Notifikation hat keine Home-Health gemeldet, algemeiner Fehler!"
		}
		log.info "$cmd"		
	}

//Powerlevel V1
//Command Class: 0x73
	//Powerlevel Report
	//Command: 0x03
	def zwaveEvent(hubitat.zwave.commands.powerlevelv1.PowerlevelReport cmd) {
		//Short powerLevel
		//Short timeout
		//static Short POWER_LEVEL_MINUS1DBM = 1
		//static Short POWER_LEVEL_MINUS2DBM = 2
		//static Short POWER_LEVEL_MINUS3DBM = 3
		//static Short POWER_LEVEL_MINUS4DBM = 4
		//static Short POWER_LEVEL_MINUS5DBM = 5
		//static Short POWER_LEVEL_MINUS6DBM = 6
		//static Short POWER_LEVEL_MINUS7DBM = 7
		//static Short POWER_LEVEL_MINUS8DBM = 8
		//static Short POWER_LEVEL_MINUS9DBM = 9
		//static Short POWER_LEVEL_NORMALPOWER = 0
		//["normal","-1 dBm","-2 dBm","-3 dBm","-4 dBm","-5 dBm","-6 dBm","-7 dBm","-8 dBm","-9 dBm"]
		def msg = ""
		def param = ""
		switch (cmd.powerLevel) {
			case 0:
				msg = "Normal Power ist eingestellt"
				param = "normal"
				break;
			case 1:
				msg = "Power ist auf -1 dBm eingestellt"
				param = "-1 dBm"
				break;
			case 2:
				msg = "Power ist auf -2 dBm eingestellt"
				param = "-2 dBm"
				break;
			case 3:
				msg = "Power ist auf -3 dBm eingestellt"
				param = "-3 dBm"
				break;
			case 4:
				msg = "Power ist auf -4 dBm eingestellt"
				param = "-4 dBm"
				break;
			case 5:
				msg = "Power ist auf -5 dBm eingestellt"
				param = "-5 dBm"
				break;
			case 6:
				msg = "Power ist auf -6 dBm eingestellt"
				param = "-6 dBm"
				break;
			case 7:
				msg = "Power ist auf -7 dBm eingestellt"
				param = "-7 dBm"
				break;
			case 8:
				msg = "Power ist auf -8 dBm eingestellt"
				param = "-8 dBm"
				break;
			case 9:
				msg = "Power ist auf -9 dBm eingestellt"
				param = "-9 dBm"
				break;
		}
		log.info "PowerlevelReport meldet gerade: ${msg}"
		sendEvent(name:"Signal-Stärke", value: param, displayed: true)
	}

//Security V1
//Command Class: 0x98
	//Security Message Encapsulation
	//Command: 0x81
	def zwaveEvent(hubitat.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) { 
		//Short commandClassIdentifier
		//Short commandIdentifier
		//Boolean secondFrame
		//Short sequenceCounter
		//Boolean sequenced
		state.sec = 1
		def encapsulatedCommand = cmd.encapsulatedCommand ([0x85:0x03, 0x85:0x06, 0x59:0x02, 0x70:0x06, 0x5A:0x01, 0x7A:0x02, 0x7A:0x07, 0x72:0x05, 0x31:0x05, 0x71:0x05, 0x73:0x03, 0x86:0x12])//([0x86:0x12,0x73:0x03,0x31:0x05,0x71:0x05,0x72:05,0x7A:0x02,0x5A:0x01,0x70:0x06,0x85:0x03,0x59:0x02]) 
		if (encapsulatedCommand) {
			return zwaveEvent(encapsulatedCommand)
		} else {
			createEvent(descriptionText: cmd.toString())
		}
	}

//Supervision V1
//Command Class: 0x6C
	//Supervision Report
	//Command: 0x02
	def zwaveEvent(hubitat.zwave.commands.supervisionv1.SupervisionReport cmd) {
		//Short duration
		//Boolean moreStatusUpdates
		//Short reserved
		//Short sessionID
		//Short status
		//static Short FAIL = 2
		//static Short NO_SUPPORT = 0
		//static Short SUCCESS = 255
		//static Short WORKING = 1
         def msg = cmd.duration.toString() + " " + cmd.moreStatusUpdates.toString() + " " + cmd.reserved.toString() + " " + cmd.sessionID.toString() + " " + cmd.status.toString()
    log.warn "$cmd"
    sendEvent(name:"Supervision", value: msg)
	}

	//Supervision Get
	//Command: 0x01
void zwaveEvent(hubitat.zwave.commands.supervisionv1.SupervisionGet cmd){
    hubitat.zwave.Command encapCmd = cmd.encapsulatedCommand(commandClassVersions)
    if (encapCmd) {
        zwaveEvent(encapCmd)
    }
    sendHubCommand(new hubitat.device.HubAction(command(zwave.supervisionV1.supervisionReport(sessionID: cmd.sessionID, reserved: 0, moreStatusUpdates: false, status: 0xFF, duration: 0)), hubitat.device.Protocol.ZWAVE))
}

//Version V2
//Command Class: 0x86
	//Version Report
	//Command: 0x12
	def zwaveEvent(hubitat.zwave.commands.versionv2.VersionReport cmd) {
		//Short applicationSubVersion
		//Short applicationVersion
		//Short firmware0SubVersion
		//Short firmware0Version
		//Short firmwareTargets
		//Short hardwareVersion
		//List<VersionReport> targetVersions
		//Short zWaveLibraryType
		//Short zWaveProtocolSubVersion
		//Short zWaveProtocolVersion
		def zWaveLibraryTypeDisp  = String.format("%02X",cmd.zWaveLibraryType)
		def zWaveLibraryTypeDesc  = ""
		switch(cmd.zWaveLibraryType) {
			case 1:
				zWaveLibraryTypeDesc = "Static Controller"
				break;
			case 2:
				zWaveLibraryTypeDesc = "Controller"
				break;
			case 3:
				zWaveLibraryTypeDesc = "Enhanced Slave"
				break;
			case 4:
				zWaveLibraryTypeDesc = "Slave"
				break;
			case 5:
				zWaveLibraryTypeDesc = "Installer"
				break;
			case 6:
				zWaveLibraryTypeDesc = "Routing Slave"
				break;
			case 7:
				zWaveLibraryTypeDesc = "Bridge Controller"
				break;
			case 8:
				zWaveLibraryTypeDesc = "Device Under Test (DUT)"
				break;
			case 0x0A:
				zWaveLibraryTypeDesc = "AV Remote"
				break;
			case 0x0B:
				zWaveLibraryTypeDesc = "AV Device"
				break;
			default:
				zWaveLibraryTypeDesc = "N/A"
		}
		def applicationVersionDisp = String.format("%d.%02d",cmd.applicationVersion,cmd.applicationSubVersion)
		def zWaveProtocolVersionDisp = String.format("%d.%02d",cmd.zWaveProtocolVersion,cmd.zWaveProtocolSubVersion)
		//sendEvent([name: "zWaveLibraryType", value:  zWaveLibraryTypeDesc])
		//sendEvent([name: "hardwareVersion", value: cmd.hardwareVersion])
		log.info "zWaveLibraryType ist $zWaveLibraryTypeDesc"
		log.info "hardwareVersion ist $cmd.hardwareVersion"
	}

//Zwaveplus Info V2
//Command Class: 0x5E
	//Zwaveplus Info Report
	//Command: 0x02
	def zwaveEvent(hubitat.zwave.commands.zwaveplusinfov2.ZwaveplusInfoReport cmd) {
		//Long installerIconType
		//Long userIconType
		//Short zWaveplusNodeType
		//Short zWaveplusRoleType
		//Short zWaveplusVersion
		def rType = ""
		switch (cmd.zWaveplusRoleType) {
			case 0x00:
				rType = "Central Static Controller"
				break;
			case 0x01:
				rType = "Sub Static Controller"
				break;
			case 0x02:
				rType = "Portable Controller"
				break;
			case 0x03:
				rType = "Reporting Portable Controller"
				break;
			case 0x04:
				rType = "Portable Slave"
				break;
			case 0x05:
				rType = "Always On Slave"
				break;
			case 0x06:
				rType = "Reporting Sleeping Slave"
				break;
			case 0x07:
				rType = "Listening Sleeping Slave"
				break;
			case 0x08:
				rType = "Network Aware Slave"
				break;
		}
		log.info "Geräterolle ist ${rType}"
		log.info "ZwaveplusInfoReport ${cmd}"
	}

def vocNotifity (int value) { 
    def msg = ""
    switch (value) {    
        case 0:    
            msg = "hervorragend"
            break;    
        case 1:    
            msg = "gut"
            break;    
        case 2:    
            msg = "mittelmäßig"
            break;    
        case 3:    
            msg = "schlecht"
            break;    
        case 4:    
            msg = "gesundheitsschädlich"
            break;    
        case 5:    
            msg = "lebensgefahr"
            break;
    }
    sendEvent (name: "VOC-Niveau", value: msg)
}

def co2Notifity (int value) {    
    def msg = ""
    switch (value) {
        case 1:
            msg = "gut"
            break;
        case 2:
            msg = "mittelmäßig"
            break;
        case 3:
            msg = "schlecht"
            break;
        case 4:
            msg = "gesundheitsschädlich"
            break;
        case 5:
            msg = "lebensgefahr"
            break;
    }
    sendEvent (name:"carbonDioxide-Niveau", value: msg)
}

def zwaveEvent(hubitat.zwave.Command cmd) {
	log.warn "Unhandled command: ${cmd}"
}

def secure(hubitat.zwave.Command cmd) {
	if (state.sec) {
		return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
	} 
    else {
    	return cmd.format()
	}
}

def secureSequence(commands, delay=1500) { 
    delayBetween(commands.collect{ secure(it) }, delay)
}

def configure() {
	def TemperaturdifferenzL 		= stringToHexList (Temperaturdifferenz)
	def FeuchtigkeitsdifferenzL 	= stringToHexList (Feuchtigkeitsdifferenz)
	def TemperatureeinheitL 		= stringToHexList (Temperatureeinheit)
	def TemperaturAufloesungL 		= stringToHexList (TemperaturAufloesung)
	def FeuchtigkeitsAufloesungL 	= stringToHexList (FeuchtigkeitsAufloesung)
	def VOCChageReportingL 			= stringToHexList (VOCChageReporting)
	def CO2ChangeReportingL 		= stringToHexList (CO2ChangeReporting)
	def LedIndikationL 				= stringToHexList (LedIndikation)		 
	def cmds = []
	def tmpE = Temperatureeinheit == "1" ? 1 : 0
    sendEvent(name: "tamper", value: "clear", displayed: false)	
    cmds << zwave.configurationV1.configurationSet(configurationValue: TemperaturdifferenzL,		parameterNumber:1, size:1) //, scaledConfigurationValue:  TemperaturdifferenzL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: FeuchtigkeitsdifferenzL, 	parameterNumber:2, size:1) //, scaledConfigurationValue:  FeuchtigkeitsdifferenzL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: TemperatureeinheitL,			parameterNumber:3, size:1) //, scaledConfigurationValue:  TemperatureeinheitL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: TemperaturAufloesungL,		parameterNumber:4, size:1) //, scaledConfigurationValue:  TemperaturAufloesungL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: FeuchtigkeitsAufloesungL,	parameterNumber:5, size:1) //, scaledConfigurationValue:  FeuchtigkeitsAufloesungL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: VOCChageReportingL,			parameterNumber:6, size:1) //, scaledConfigurationValue:  VOCChageReportingL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: CO2ChangeReportingL,			parameterNumber:7, size:1) //, scaledConfigurationValue:  CO2ChangeReportingL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: LedIndikationL,				parameterNumber:8, size:1) //, scaledConfigurationValue:  LedIndikationL.get(0))
	cmds << zwave.powerlevelV1.powerlevelGet()
	cmds << zwave.associationV2.associationGet (groupingIdentifier:1)
	cmds << zwave.associationV2.associationGet (groupingIdentifier:2)
	sendEvent(name: "Configuration", value: "sent", displayed: true)
	sendHubCommand(new hubitat.device.HubAction(new hubitat.zwave.commands.zwaveplusinfov2.ZwaveplusInfoGet().format(), hubitat.device.Protocol.ZWAVE))
    log.info "Configure sent"
	secureSequence(cmds)
}

def poll() {
	log.info "Sensorpolling"
    def cmds = []
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     			//get temperature
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x05, scale: 0)     	    		//get humidity
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     			//get dewpoint
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x11, scale: 0)     	    		//get carbon dioxide
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x27, scale: 1)					//get voc
    cmds << zwave.notificationV8.notificationGet(event:0x06, notificationType:0x0D, v1AlarmType:0x00)	//get home health
	secureSequence(cmds)
}


def stringToHexList(String value) {
	switch (value) {
		case "-128": return [0x80]
		case "-50" : return [0xCE]
		case "-49" : return [0xCF]
		case "-48" : return [0xD0]
		case "-47" : return [0xD1]
		case "-46" : return [0xD2]
		case "-45" : return [0xD3]
		case "-44" : return [0xD4]
		case "-43" : return [0xD5]
		case "-42" : return [0xD6]
		case "-41" : return [0xD7]
		case "-40" : return [0xD8]
		case "-39" : return [0xD9]
		case "-38" : return [0xDA]
		case "-37" : return [0xDB]
		case "-36" : return [0xDC]
		case "-35" : return [0xDD]
		case "-34" : return [0xDE]
		case "-33" : return [0xDF]
		case "-32" : return [0xE0]
		case "-31" : return [0xE1]
		case "-30" : return [0xE2]
		case "-29" : return [0xE3]
		case "-28" : return [0xE4]
		case "-27" : return [0xE5]
		case "-26" : return [0xE6]
		case "-25" : return [0xE7]
		case "-24" : return [0xE8]
		case "-23" : return [0xE9]
		case "-22" : return [0xEA]
		case "-21" : return [0xEB]
		case "-20" : return [0xEC]
		case "-19" : return [0xED]
		case "-18" : return [0xEE]
		case "-17" : return [0xEF]
		case "-16" : return [0xF0]
		case "-15" : return [0xF1]
		case "-14" : return [0xF2]
		case "-13" : return [0xF3]
		case "-12" : return [0xF4]
		case "-11" : return [0xF5]
		case "-10" : return [0xF6]
		case "-9"  : return [0xF7]
		case "-8"  : return [0xF8]
		case "-7"  : return [0xF9]
		case "-6"  : return [0xFA]
		case "-5"  : return [0xFB]
		case "-4"  : return [0xFC]
		case "-3"  : return [0xFD]
		case "-2"  : return [0xFE]
		case "-1"  : return [0xFF]
		case "0"   : return [0x00]
		case "1"   : return [0x01]
		case "2"   : return [0x02]
		case "3"   : return [0x03]
		case "4"   : return [0x04]
		case "5"   : return [0x05]
		case "6"   : return [0x06]
		case "7"   : return [0x07]
		case "8"   : return [0x08]
		case "9"   : return [0x09]
		case "10"  : return [0x0A]
		case "11"  : return [0x0B]
		case "12"  : return [0x0C]
		case "13"  : return [0x0D]
		case "14"  : return [0x0E]
		case "15"  : return [0x0F]
		case "16"  : return [0x10]
		case "17"  : return [0x11]
		case "18"  : return [0x12]
		case "19"  : return [0x13]
		case "20"  : return [0x14]
		case "21"  : return [0x15]
		case "22"  : return [0x16]
		case "23"  : return [0x17]
		case "24"  : return [0x18]
		case "25"  : return [0x19]
		case "26"  : return [0x1A]
		case "27"  : return [0x1B]
		case "28"  : return [0x1C]
		case "29"  : return [0x1D]
		case "30"  : return [0x1E]
		case "31"  : return [0x1F]
		case "32"  : return [0x20]
		case "33"  : return [0x21]
		case "34"  : return [0x22]
		case "35"  : return [0x23]
		case "36"  : return [0x24]
		case "37"  : return [0x25]
		case "38"  : return [0x26]
		case "39"  : return [0x27]
		case "40"  : return [0x28]
		case "41"  : return [0x29]
		case "42"  : return [0x2A]
		case "43"  : return [0x2B]
		case "44"  : return [0x2C]
		case "45"  : return [0x2D]
		case "46"  : return [0x2E]
		case "47"  : return [0x2F]
		case "48"  : return [0x30]
		case "49"  : return [0x31]
		case "50"  : return [0x32]
        case "51"  : return [0x33]
        case "52"  : return [0x34]
        case "53"  : return [0x35]
        case "54"  : return [0x36]
        case "55"  : return [0x37]
        case "56"  : return [0x38]
        case "57"  : return [0x39]
        case "58"  : return [0x3A]
        case "59"  : return [0x3B]
        case "60"  : return [0x3C]
        case "61"  : return [0x3D]
        case "62"  : return [0x3E]
        case "63"  : return [0x3F]
        case "64"  : return [0x40]
        case "65"  : return [0x41]
        case "66"  : return [0x42]
        case "67"  : return [0x43]
        case "68"  : return [0x44]
        case "69"  : return [0x45]
        case "70"  : return [0x46]
        case "71"  : return [0x47]
        case "72"  : return [0x48]
        case "73"  : return [0x49]
        case "74"  : return [0x4A]
        case "75"  : return [0x4B]
        case "76"  : return [0x4C]
        case "77"  : return [0x4D]
        case "78"  : return [0x4E]
        case "79"  : return [0x4F]
        case "80"  : return [0x50]
        case "81"  : return [0x51]
        case "82"  : return [0x52]
        case "83"  : return [0x53]
        case "84"  : return [0x54]
        case "85"  : return [0x55]
        case "86"  : return [0x56]
        case "87"  : return [0x57]
        case "88"  : return [0x58]
        case "89"  : return [0x59]
        case "90"  : return [0x5A]
        case "91"  : return [0x5B]
        case "92"  : return [0x5C]
        case "93"  : return [0x5D]
        case "94"  : return [0x5E]
        case "95"  : return [0x5F]
        case "96"  : return [0x60]
        case "97"  : return [0x61]
        case "98"  : return [0x62]
        case "99"  : return [0x63]
		case "100" : return [0x64]
		case "101" : return [0x65]
		case "102" : return [0x66]
		case "103" : return [0x67]
		case "104" : return [0x68]
		case "105" : return [0x69]
		case "106" : return [0x6A]
		case "107" : return [0x6B]
		case "108" : return [0x6C]
		case "109" : return [0x6D]
		case "110" : return [0x6E]
		case "111" : return [0x6F]
		case "112" : return [0x70]
		case "113" : return [0x71]
		case "114" : return [0x72]
		case "115" : return [0x73]
		case "116" : return [0x74]
		case "117" : return [0x75]
		case "118" : return [0x76]
		case "119" : return [0x77]
		case "120" : return [0x78]
		case "121" : return [0x79]
		case "122" : return [0x7A]
		case "123" : return [0x7B]
		case "124" : return [0x7C]
		case "125" : return [0x7D]
		case "126" : return [0x7E]
		case "127" : return [0x7F]
		default: return null
	}
}

def setSignalStaerke (signalStaerke) {
	def cmds = [ ]
	def sgnl = 0
	switch (signalStaerke) {
		case "normal":
			sgnl = 0
			break;
		case "-1 dBm":
			sgnl = 1
			break;
		case "-2 dBm":
			sgnl = 2
			break;
		case "-3 dBm":
			sgnl = 3
			break;
		case "-4 dBm":
			sgnl = 4
			break;
		case "-5 dBm":
			sgnl = 5
			break;
		case "-6 dBm":
			sgnl = 6
			break;
		case "-7 dBm":
			sgnl = 7
			break;
		case "-8 dBm":
			sgnl = 8
			break;
		case "-9 dBm":
			sgnl = 9
			break;
	}
	cmds << zwave.powerlevelV1.powerlevelSet (powerLevel: sgnl)
	cmds << zwave.powerlevelV1.powerlevelGet ()
	log.info "Singalstärke wird auf ${signalStaerke} eingestellt"
	secureSequence (cmds)
}

def setAssociationGroup(group, nodes, action){
   // Normalize the arguments to be backwards compatible with the old method
    group  = "${group}" =~ /\d+/ ? (group as int) : group                             // convert group to int (if possible)
    maxAssoz = group == 1 ? 1 : 5
	def cmds = []
	count = 0
	action = action == 1 ? "Add" : action == 0 ? "Remove": action
		
    if (! nodes.every { it =~ /[0-9]+/ }) {
        log.error "${device.label?device.label:device.name}: invalid Nodes ${nodes}"
        return
    }

    if (group < 1 || group > 2) {
        log.error "${device.label?device.label:device.name}: Association group is invalid 1 <= ${group} <= ${maxAssociationGroup()}"
        return
    }
	
	if (state."actualAssociation${group}" != null) {
		count = state."actualAssociation${group}".size()
	}
    
	log.debug "nodes ist $nodes"
	
	if (nodes != null) {
		log.debug "nodes ist nicht null"
		nodes.each {
			log.debug "nodes each funktioniert"
			log.debug "aktion ist $action"
			switch (action) {
				case "Add"	:
				if (count >= maxAssoz) {
					log.warn "Group $group ist voll. Keine Aufnahme mehr möglich"
				} else {	
					log.debug "Add von $it"
					cmds << zwave.associationV2.associationSet(groupingIdentifier:group, nodeId:it.toInteger())
					count = count + 1
				}
				break;
				case "Remove":
				if (count <= 0) {
					log.warn "Group $group ist leer."
				} else {
					log.debug "Del von $it"
					cmds << zwave.associationV2.associationRemove(groupingIdentifier:group, nodeId:it.toInteger())
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
	
	cmds << zwave.associationV2.associationGet(groupingIdentifier:group)
	
    secureSequence (cmds)
}
