//Version 1.0 Release

import hubitat.device.HubAction
import hubitat.device.Protocol

metadata {
	definition(name: "Luftgutesensor Eurotronic", namespace: "forgetiger55122", author: "Ravil Rubashkin", mnmn:"SmartThingsCommunity", vid:"e1fa8d53-5d1a-3d14-9329-fa156189e663" ) { //"9b37cdaf-0cdb-3643-94cf-867460ea67e6" "e1fa8d53-5d1a-3d14-9329-fa156189e663"
	    capability "CarbonDioxideMeasurement"
        capability "TemperatureMeasurement"
		capability "Relative Humidity Measurement"
		capability "Sensor"
        capability "Configuration"
        capability "TamperAlert"
        capability "Polling"		
        
        attribute "VOC", 				"number"																			//ohne Messeinheit
        attribute "DewPoint", 			"number" 																			//ohne Messeinheit   
        attribute "TVOC", 				"string"																			//mit Messeinheit
        attribute "Dew Point", 			"string" 																			//mit Messeinheit
        attribute "VOC Niveau", 		"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]	//Interpretation
        attribute "CO2 Niveau", 		"enum", ["hervorragend","gut","mittelmäßig","gesundheitsschädlich","lebensgefahr"]	//Interpretation
		attribute "AssotiationsList1",	"list"																				//AssotiationsListe Group 1
		attribute "AssotiationsList2",	"list"																				//AssotiationsListe Group 2
		
		command "addZuAssociationsliste", 	[[name:"nodeID*", type: "NUMBER", description: "nodeID vom hizufügenden Gerät"]]
		command "delVomAssociationsliste", 	[[name:"nodeID*", type: "NUMBER", description: "nodeID vom hizufügenden Gerät"]]
        
		fingerprint mfr:"0148", prod:"0005", model:"0001"        
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
        input "Temperaturdifferenz",     "enum", title: "Temperatur on Change Reporting", 	options: tempReportRates, 	description: "Default: 0.5°C", 							required: false, displayDuringSetup: true
        input "Feuchtigkeitsdifferenz",  "enum", title: "Feuchtigkeit on Change Reporting",	options: humReportRates, 	description: "Default: 5%", 							required: false, displayDuringSetup: true
        input "Temperatureeinheit",      "enum", title: "Temperatureeinheit", 				options: tempEinheit, 		description: "Default: °C", 							required: false, displayDuringSetup: true
        input "TemperaturAufloesung",    "enum", title: "Auflösung Temperatur", 			options: tempAufloesung, 	description: "Default: Eine Nachkommastelle", 			required: false, displayDuringSetup: true
        input "FeuchtigkeitsAufloesung", "enum", title: "Auflösung Feuchte", 				options: humAufloesung, 	description: "Default: Keine Nachkommastelle", 			required: false, displayDuringSetup: true
        input "VOCChageReporting",       "enum", title: "VOC-on Change Reporting", 			options: vocChangeReport, 	description: "Default: 0.5 ppm", 						required: false, displayDuringSetup: true
        input "CO2ChangeReporting",      "enum", title: "CO2 Change Reporting", 			options: co2ChangeReport, 	description: "Default: 500 ppm", 						required: false, displayDuringSetup: true
        input "LedIndikation",           "enum", title: "Luftgüte per LED signalisieren", 	options: ledIndikator, 		description: "Default: Luftgüte per Led signalisieren", required: false, displayDuringSetup: true   
	}

}

def installed() {    
    sendEvent(name: "tamper", value: "clear", displayed: false)
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
	response([
		secureSequence(zwave.notificationV8.notificationGet(notificationType: 0x0D)), 	                  // Home Health
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
    return results
}

//Association V2
//Command Class: 0x85
	//Association Report
	//Command: 0x03
	def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
		//Short groupingIdentifier
		//Short maxNodesSupported
		//Short reportsToFollow
		if (cmd.groupingIdentifier == 1) {
			sendEvent(name:AssotiationsList1, value: cmd.nodeId, displayed: true)
		}
		if (cmd.groupingIdentifier == 2) {
			def grosse = cmd.nodeId.size
			sendEvent(name:AssotiationsList2, value: cmd.nodeId, displayed: true)
			state.associationsListe = grosse			
		}
		log.info "AssociationReport ist ${cmd}"
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
		sendEvent(name: "configuration", value: "receive", displayed: true)
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
				log.info "Temperaturreport ist ${tempReportRates[msg]}"
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
				log.info "Feuchtigkeitsreport ist ${humReportRates[msg]}"
				break;				
			case 3:
				def tempEinheit = [:]
					tempEinheit << ["0":"Celcius"   ]
					tempEinheit << ["1":"Fahrenheit"]
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "Temperatureinheit ist ${tempEinheit[msg]}"
				break;				
			case 4:
				def tempAufloesung = [:]
					tempAufloesung << ["0":"Keine Nachkommastelle"] 
					tempAufloesung << ["1":"Eine Nachkommastelle" ]  
					tempAufloesung << ["2":"Zwei Nachkommastellen"] 
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "Temperaturauflösung ist ${tempAufloesung[msg]}"
				break;				
			case 5:
				def humAufloesung = [:]
					humAufloesung << ["0":"Keine Nachkommastelle"]  
					humAufloesung << ["1":"Eine Nachkommastelle" ]   
					humAufloesung << ["2":"Zwei Nachkommastellen"]  
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "Feuchtigkeitsauflösung ist ${humAufloesung[msg]}"
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
				break;				
			case 8:
				def ledIndikator = [:]
					ledIndikator << ["0":"Led aus"]
					ledIndikator << ["1":"Led ein"]
				def msg = cmd.scaledConfigurationValue.toString()
				log.info "${ledIndikator[msg]}"
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
				map.value = cmd.scaledSensorValue.toFloat()
				log.info "Temperature ist ${map.value}"
				break;
			case 0x05:
				map.name = "humidity"			
				map.unit = cmd.scale == 1 ? "g/m^3" : "%"
				map.value = cmd.scaledSensorValue.toFloat()
				log.info "Feuchtigkeit ist ${map.value}"
				break;
			case 0x0B:
				map.name = "DewPoint"
				map.unit = cmd.scale == 1 ? "°F" : "°C"
				map.value = cmd.scaledSensorValue.toFloat()
				log.info "Dew Point ist ${map.value}"
				msg = map.value.toString() + " " + map.unit
				sendEvent (name:"Dew Point", value: msg)
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
				msg = map.value.toString() + " " + map.unit
				sendEvent(name:"TVOC", value: msg)
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
		log.info "Home Health hat Niveau ${cmd.event} gemeldet"
		if (cmd.notificationType == 0x0D) {
			switch (cmd.event) {
			case 0x01:
				vocNotifity (1)
				break;
			case 0x02:
				vocNotifity (2)
				break;
			case 0x03:
				vocNotifity (3)
				break;
			case 0x04:
				vocNotifity (4)
				break;         
			}
		}
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
		def msg = ""
		switch (cmd.powerLevel) {
			case 0:
				msg = "Normal Power ist eingestellt"
				break;
			case 1:
				msg = "Power ist auf -1 dBm eingestellt"
				break;
			case 2:
				msg = "Power ist auf -2 dBm eingestellt"
				break;
			case 3:
				msg = "Power ist auf -3 dBm eingestellt"
				break;
			case 4:
				msg = "Power ist auf -4 dBm eingestellt"
				break;
			case 5:
				msg = "Power ist auf -5 dBm eingestellt"
				break;
			case 6:
				msg = "Power ist auf -6 dBm eingestellt"
				break;
			case 7:
				msg = "Power ist auf -7 dBm eingestellt"
				break;
			case 8:
				msg = "Power ist auf -8 dBm eingestellt"
				break;
			case 9:
				msg = "Power ist auf -9 dBm eingestellt"
				break;
		}
		log.info "PowerlevelReport meldet gerade: ${msg}"
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
		def encapsulatedCommand = cmd.encapsulatedCommand ([0x86:0x12,0x73:0x03,0x31:0x05,0x71:0x05,0x72:05,0x7A:0x02,0x5A:0x01,0x70:0x06,0x85:0x03,0x59:0x04]) 
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
		log.info "SupervisionReport ${cmd}"
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
		sendEvent([name: "applicationVersion", value:  applicationVersionDisp])
		sendEvent([name: "zWaveLibraryType", value:  zWaveLibraryTypeDesc])
		sendEvent([name: "firmware0Version", value: cmd.firmware0Version])
		sendEvent([name: "hardwareVersion", value: cmd.hardwareVersion])
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
    sendEvent (name: "VOC Niveau", value: msg)
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
    sendEvent (name:"CO2 Niveau", value: msg)
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
    cmds << zwave.configurationV1.configurationSet(configurationValue: TemperaturdifferenzL,		parameterNumber:1, size:1, scaledConfigurationValue:  TemperaturdifferenzL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: FeuchtigkeitsdifferenzL, 	parameterNumber:2, size:1, scaledConfigurationValue:  FeuchtigkeitsdifferenzL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: TemperatureeinheitL,			parameterNumber:3, size:1, scaledConfigurationValue:  TemperatureeinheitL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: TemperaturAufloesungL,		parameterNumber:4, size:1, scaledConfigurationValue:  TemperaturAufloesungL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: FeuchtigkeitsAufloesungL,	parameterNumber:5, size:1, scaledConfigurationValue:  FeuchtigkeitsAufloesungL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: VOCChageReportingL,			parameterNumber:6, size:1, scaledConfigurationValue:  VOCChageReportingL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: CO2ChangeReportingL,			parameterNumber:7, size:1, scaledConfigurationValue:  CO2ChangeReportingL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: LedIndikationL,				parameterNumber:8, size:1, scaledConfigurationValue:  LedIndikationL.get(0))
	cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     //get temperature
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x05, scale: 0)     	//get humidity
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     //get dewpoint
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x11, scale: 0)     	//get carbon dioxide
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x27, scale: 1)     	//get voc    
	cmds << zwave.configurationV1.configurationGet(parameterNumber:1)               		//get pamam 1
	cmds << zwave.configurationV1.configurationGet(parameterNumber:2)               		//get pamam 2
	cmds << zwave.configurationV1.configurationGet(parameterNumber:3)               		//get pamam 3
	cmds << zwave.configurationV1.configurationGet(parameterNumber:4)               		//get pamam 4
	cmds << zwave.configurationV1.configurationGet(parameterNumber:5)               		//get pamam 5
	cmds << zwave.configurationV1.configurationGet(parameterNumber:6)               		//get pamam 6
	cmds << zwave.configurationV1.configurationGet(parameterNumber:7)               		//get pamam 7
	cmds << zwave.configurationV1.configurationGet(parameterNumber:8)	               		//get pamam 8
	cmds << zwave.versionV2.versionGet()
	cmds << zwave.associationV2.associationGet(groupingIdentifier:1)
	cmds << zwave.associationV2.associationGet(groupingIdentifier:2)
	cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier:1)
	cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier:2)	
	cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet()      
	cmds << zwave.powerlevelV1.powerlevelGet()
	cmds << zwave.versionV2.versionGet()
	//cmds << zwave.zwaveplusInfoV2.zwaveplusInfoGet() 
    cmds << zwave.notificationV8.notificationGet(notificationType:13)
	sendEvent(name: "configuration", value: "sent", displayed: true)
	secureSequence(cmds)
}

def updated() {
	unschedule(poll)	
}

def poll() {
	log.info "Sensorpolling"
    def cmds = []
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     	//get temperature
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x05, scale: 0)     	    //get humidity
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     	//get dewpoint
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x11, scale: 0)     	    //get carbon dioxide
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x27, scale: 1)			//get voc
	cmds << zwave.associationV2.associationGet(groupingIdentifier:2)
	secureSequence (cmds)
}


def stringToHexList(String value) {
	switch (value) {
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
		case "128" : return [0x80]	
		default: return null
	}
}

//Dies ist nötig zur Assotiation von Geräten.
//Beispiel ist TRV von Eurotronic
//Wenn heir NodeID von TRV angegeben wirdn und TRV in externe Temperatur-Modus gestellt, wird externe Temperatur zur Steuerung genommen.
def addZuAssociationsliste (BigDecimal nID) {
	//hubitat.zwave.commands.associationv2.AssociationSet {
	//Short groupingIdentifier
	//Object nodeId
	def cmds = []
	if (state.associationsliste < 5) {
		log.info "Die Aufnahme vom Gerät mit NodeID:${nID} wird vorgenommen"
		cmds << zwave.associationV2.associationSet (groupingIdentifier:2, nodeId: nID)
	} else {
		log.warn "Es ist schon maximale Anzahl an Geräten auf der Associationsliste erreicht. Noch eine Aufnahme nicht möglich"
	}
	cmds << zwave.associationV2.associationGet (groupingIdentifier:2)
	secureSequence (cmds)
}

//Genau das Gegenteil vom Programm von Oben
def delVomAssociationsliste (BigDecimal nID) {
	//hubitat.zwave.commands.associationv2.AssociationRemove {
	//Short groupingIdentifier
	//Object nodeId
	def cmds = []
	if (state.associationsliste > 0) {
		log.info "Die Entfernung vom Gerät mit NodeID:${nID} wird vorgenommen"
		cmds << zwave.associationV2.associationRemove (groupingIdentifier:2, nodeId: nID)
	} else {
		log.warn "Es ist kein Gerät auf der Liste"
	}
	cmds << zwave.associationV2.associationGet (groupingIdentifier:2)
	secureSequence (cmds)
}
