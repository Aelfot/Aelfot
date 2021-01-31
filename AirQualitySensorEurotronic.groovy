//to do assoziation!

import hubitat.device.HubAction
import hubitat.device.Protocol

metadata {
	definition(name: "Luftgutesensor Eurotronic", namespace: "forgetiger55122", author: "Ravil Rubashkin", mnmn:"SmartThingsCommunity", vid:"e1fa8d53-5d1a-3d14-9329-fa156189e663" ) { //"9b37cdaf-0cdb-3643-94cf-867460ea67e6" "e1fa8d53-5d1a-3d14-9329-fa156189e663"
	    capability "CarbonDioxideMeasurement"
        capability "Temperature Measurement"
		capability "Relative Humidity Measurement"
		capability "Sensor"
        capability "Health Check"
        capability "Configuration"
        capability "TamperAlert"
        capability "Polling"
        
        attribute "VOC", "number"                                                                                                //ohne Messeinheit
        attribute "DewPoint", "number"                                                                                           //ohne Messeinheit   
        attribute "TVOC", "string"                                                                                               //mit Messeinheit
        attribute "Dew Point", "string"                                                                                          //mit Messeinheit
        attribute "VOC-Niveau", "enum", ["hervorragend","gut","mittelmäßig","schlecht","gesundheitschädlich","lebensgefahr"]     //Interpretation
        attribute "CO2-Niveau", "enum", ["hervorragend","gut","mittelmäßig","schlecht","gesundheitschädlich","lebensgefahr"]     //Interpretation
        
		fingerprint mfr:"0148", prod:"0005", model:"0001"        
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

    def checkIntervals = [:]
        checkIntervals << ["0":"1 mal pro 24 Stunden"]        //health check
        checkIntervals << ["1":"Abfrage jede Minute"]
        checkIntervals << ["2":"Abfrage alle 2 Minuten"]
        checkIntervals << ["3":"Abfrage alle 2 Minuten"]
        checkIntervals << ["4":"Abfrage alle 4 Minuten"]
        checkIntervals << ["5":"Abfrage alle 5 Minuten"]
        checkIntervals << ["10":"Abfrage alle 10 Minuten"]
        checkIntervals << ["15":"Abfrage alle 15 Minuten"]
        checkIntervals << ["30":"Abfrage alle 30 Minuten"]
        checkIntervals << ["60":"Abfrage stündlich"]

   
    preferences {
        input "Temperaturdifferenz",     "enum", title: "Temperatur on Change Reporting", 	options: tempReportRates, 	description: "Default: 0.5°C", 							required: false, displayDuringSetup: true
        input "Feuchtigkeitsdifferenz",  "enum", title: "Feuchtigkeit on Change Reporting",	options: humReportRates, 	description: "Default: 5%", 							required: false, displayDuringSetup: true
        input "Temperatureeinheit",      "enum", title: "Temperatureeinheit", 				options: tempEinheit, 		description: "Default: °C", 							required: false, displayDuringSetup: true
        input "TemperaturAufloesung",    "enum", title: "Auflösung Temperatur", 			options: tempAufloesung, 	description: "Default: Eine Nachkommastelle", 			required: false, displayDuringSetup: true
        input "FeuchtigkeitsAufloesung", "enum", title: "Auflösung Feuchte", 				options: humAufloesung, 	description: "Default: Keine Nachkommastelle", 			required: false, displayDuringSetup: true
        input "VOCChageReporting",       "enum", title: "VOC-on Change Reporting", 			options: vocChangeReport, 	description: "Default: 0.5 ppm", 						required: false, displayDuringSetup: true
        input "CO2ChangeReporting",      "enum", title: "CO2 Change Reporting", 			options: co2ChangeReport, 	description: "Default: 500 ppm", 						required: false, displayDuringSetup: true
        input "LedIndikation",           "enum", title: "Luftgüte per LED signalisieren", 	options: ledIndikator, 		description: "Default: Luftgüte per Led signalisieren", required: false, displayDuringSetup: true   
		input "Checking",                "enum", title: "Check Interval",                   options: checkIntervals,    description: "Default: Keine Prüfung",                  required: false, displayDuringSetup: true
    }

}

def installed() {
    def chck = 0
    switch (Checking) {        
        case "1":
            chck = 1*60
            break;        
        case "2":
            chck = 2*60
            break;        
        case "3":
            chck = 3*60
            break;        
        case "4":
            chck = 4*60
            break;        
        case "5":
            chck = 5*60
            break;        
        case "10":
            chck = 10*60
            break;        
        case "15":
            chck = 15*60
            break;        
        case "30":
            chck = 30*60
            break;        
        case "60":
            chck = 60*60
            break;        
        default:
            chck = 24*60*60        
    }
    sendEvent(name: "checkInterval", value: chck, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
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

def zwaveEvent(hubitat.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
    createEvent(name:"tamper", value: "detected", displayed: true)
}

def zwaveEvent(hubitat.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) { 
    state.sec = 1
	def encapsulatedCommand = cmd.encapsulatedCommand ([0x85:2, 0x59:2, 0x70:1, 0x5A:1, 0x7A:3, 0x72:1, 0x31:10, 0x71:8, 0x73:1, 0x86:2]) //Clasen mit Verschlüsselung. Wichtig immer Überprüfen!
	if (encapsulatedCommand) {
		return zwaveEvent(encapsulatedCommand)
	} else {
    	createEvent(descriptionText: cmd.toString())
	}
}

def vocNotifity (int value) { //Interpretation, aber, nicht funktioniert
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
    createEvent (name: "VOC-Niveau", value: msg, displayed: true)
}

def co2Notifity (int value) {    //Interpretation, aber, nicht funktioniert
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
    createEvent (name:"CO2-Niveau", value: msg, displayed: true)
}

def zwaveEvent(hubitat.zwave.commands.notificationv8.NotificationReport cmd) {
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

def zwaveEvent(hubitat.zwave.commands.sensormultilevelv10.SensorMultilevelReport cmd) {
	def map = [:]
    def rund = 0
    def result = []
    def msg = ""
    switch (cmd.sensorType) {
		case 0x01:
			map.name = "temperature"
            map.unit = cmd.scale == 1 ? "°F" : "°C"
            map.value = cmd.scaledSensorValue.toFloat()
        	break;
		case 0x05:
			map.name = "humidity"			
			map.unit = cmd.scale == 1 ? "g/m^3" : "%"
            map.value = cmd.scaledSensorValue.toFloat()
			break;
		case 0x0B:
        	map.name = "DewPoint"
            map.unit = cmd.scale == 1 ? "°F" : "°C"
            map.value = cmd.scaledSensorValue.toFloat() 
            msg = map.value.toString() + " " + map.unit
            sendEvent (name:"Dew Point", value: msg)
           	break;
        case 0x11:
        	map.name = "carbonDioxide"
            map.unit = "ppm"
            map.value = cmd.scaledSensorValue.toInteger()
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


def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
	def result = []
	if (cmd.nodeId.any { it == zwaveHubNodeId }) {
		result << sendEvent(descriptionText: "$device.displayName is associated in group ${cmd.groupingIdentifier}")
	} else if (cmd.groupingIdentifier == 1) {
		result << sendEvent(descriptionText: "Associating $device.displayName in group ${cmd.groupingIdentifier}")
		result << response(zwave.associationV1.associationSet(groupingIdentifier:cmd.groupingIdentifier, nodeId:zwaveHubNodeId))
    } else if (cmd.groupingIdentifier == 2) {
        result << sendEvent(descriptionText: "Associating $device.displayName in group ${cmd.groupingIdentifier}")
		result << response(zwave.associationV1.associationSet(groupingIdentifier:cmd.groupingIdentifier, nodeId:zwaveHubNodeId))
    }
	log.info "Report Received : $cmd"
	result
}

def zwaveEvent (hubitat.zwave.commands.manufacturerspecificv1.ManufacturerSpecificReport cmd) {
	if (cmd.manufacturerName) { updateDataValue("manufacturer", cmd.manufacturerName) }
	if (cmd.productTypeId) { updateDataValue("productTypeId", cmd.productTypeId.toString()) }
	if (cmd.productId) { updateDataValue("productId", cmd.productId.toString()) }
	if (cmd.manufacturerId){ updateDataValue("manufacturerId", cmd.manufacturerId.toString()) }
}

def zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd ) {
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

def zwaveEvent(hubitat.zwave.commands.versionv2.VersionReport cmd) {
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
    sendEvent([name: "applicationSubVersion", value: cmd.applicationSubVersion])
    sendEvent([name: "firmwareSubVersion", value: cmd.firmware0SubVersion])
    sendEvent([name: "firmware0Version", value: cmd.firmware0Version])
    sendEvent([name: "hardwareVersion", value: cmd.hardwareVersion])
} 

def configure() {
	def cmds = []
    log.debug ("1")
	def tmpE = Temperatureeinheit == "1" ? 1 : 0
    def chck = 0
    switch (Checking) {        
        case "1":
            chck = 1*60
            break;
        case "2":
            chck = 2*60
            break;        
        case "3":
            chck = 3*60
            break;        
        case "4":
            chck = 4*60
            break;        
        case "5":
            chck = 5*60
            break;        
        case "10":
            chck = 10*60
            break;        
        case "15":
            chck = 15*60
            break;        
        case "30":
            chck = 30*60
            break;        
        case "60":
            chck = 60*60
            break;
        default:
            chck = 24*60*60        
    }
    sendEvent(name: "checkInterval", value: chck, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
    cmds << zwave.configurationV1.configurationSet(configurationValue:  Temperaturdifferenz == "0" ? [0x00] : Temperaturdifferenz == "1" ? [0x01] : Temperaturdifferenz == "2" ? [0x02] : Temperaturdifferenz == "3" ? [0x03] : Temperaturdifferenz == "4" ? [0x04] : Temperaturdifferenz == "10" ? [0x0A] : Temperaturdifferenz == "6" ? [0x06] : Temperaturdifferenz == "7" ? [0x07] : Temperaturdifferenz == "8" ? [0x08] : Temperaturdifferenz == "9" ? [0x09] : [0x05], 								parameterNumber:1, size:1, scaledConfigurationValue:  Temperaturdifferenz == "0" ? 0x00 : Temperaturdifferenz == "1" ? 0x01 : Temperaturdifferenz == "2" ? 0x02 : Temperaturdifferenz == "3" ? 0x03 : Temperaturdifferenz == "4" ? 0x04 : Temperaturdifferenz == "10" ? 0x0A : Temperaturdifferenz == "6" ? 0x06 : Temperaturdifferenz == "7" ? 0x07 : Temperaturdifferenz == "8" ? 0x08 : Temperaturdifferenz == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  Feuchtigkeitsdifferenz == "0" ? [0x00] : Feuchtigkeitsdifferenz == "1" ? [0x01] : Feuchtigkeitsdifferenz == "2" ? [0x02] : Feuchtigkeitsdifferenz == "3" ? [0x03] : Feuchtigkeitsdifferenz == "4" ? [0x04] : Feuchtigkeitsdifferenz == "10" ? [0x0A] : Feuchtigkeitsdifferenz == "6" ? [0x06] : Feuchtigkeitsdifferenz == "7" ? [0x07] : Feuchtigkeitsdifferenz == "8" ? [0x08] : Feuchtigkeitsdifferenz == "9" ? [0x09] : [0x05], 	parameterNumber:2, size:1, scaledConfigurationValue:  Feuchtigkeitsdifferenz == "0" ? 0x00 : Feuchtigkeitsdifferenz == "1" ? 0x01 : Feuchtigkeitsdifferenz == "2" ? 0x02 : Feuchtigkeitsdifferenz == "3" ? 0x03 : Feuchtigkeitsdifferenz == "4" ? 0x04 : Feuchtigkeitsdifferenz == "10" ? 0x0A : Feuchtigkeitsdifferenz == "6" ? 0x06 : Feuchtigkeitsdifferenz == "7" ? 0x07 : Feuchtigkeitsdifferenz == "8" ? 0x08 : Feuchtigkeitsdifferenz == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  Temperatureeinheit == "1" ? [0x01] : [0x00], 																																																																																														parameterNumber:3, size:1, scaledConfigurationValue:  Temperatureeinheit == "1" ? 0x01 : 0x00)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  TemperaturAufloesung == "0" ? [0x00] : TemperaturAufloesung == "2" ? [0x02] : [0x01], 																																																																																				parameterNumber:4, size:1, scaledConfigurationValue:  TemperaturAufloesung == "0" ? 0x00 : TemperaturAufloesung == "2" ? 0x02 : 0x01)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  FeuchtigkeitsAufloesung == "2" ? [0x02] : FeuchtigkeitsAufloesung == "1" ? [0x01] : [0x00], 																																																																																		parameterNumber:5, size:1, scaledConfigurationValue:  FeuchtigkeitsAufloesung == "2" ? 0x02 : FeuchtigkeitsAufloesung == "1" ? 0x01 : 0x00)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  VOCChageReporting == "0" ? [0x00] : VOCChageReporting == "1" ? [0x01] : VOCChageReporting == "2" ? [0x02] : VOCChageReporting == "3" ? [0x03] : VOCChageReporting == "4" ? [0x04] : VOCChageReporting == "10" ? [0x0A] : VOCChageReporting == "6" ? [0x06] : VOCChageReporting == "7" ? [0x07] : VOCChageReporting == "8" ? [0x08] : VOCChageReporting == "9" ? [0x09] : [0x05], 													parameterNumber:6, size:1, scaledConfigurationValue:  VOCChageReporting == "0" ? 0x00 : VOCChageReporting == "1" ? 0x01 : VOCChageReporting == "2" ? 0x02 : VOCChageReporting == "3" ? 0x03 : VOCChageReporting == "4" ? 0x04 : VOCChageReporting == "10" ? 0x0A : VOCChageReporting == "6" ? 0x06 : VOCChageReporting == "7" ? 0x07 : VOCChageReporting == "8" ? 0x08 : VOCChageReporting == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  CO2ChangeReporting == "0" ? [0x00] : CO2ChangeReporting == "1" ? [0x01] : CO2ChangeReporting == "2" ? [0x02] : CO2ChangeReporting == "3" ? [0x03] : CO2ChangeReporting == "4" ? [0x04] : CO2ChangeReporting == "10" ? [0x0A] : CO2ChangeReporting == "6" ? [0x06] : CO2ChangeReporting == "7" ? [0x07] : CO2ChangeReporting == "8" ? [0x08] : CO2ChangeReporting == "9" ? [0x09] : [0x05], 											parameterNumber:7, size:1, scaledConfigurationValue:  CO2ChangeReporting == "0" ? 0x00 : CO2ChangeReporting == "1" ? 0x01 : CO2ChangeReporting == "2" ? 0x02 : CO2ChangeReporting == "3" ? 0x03 : CO2ChangeReporting == "4" ? 0x04 : CO2ChangeReporting == "10" ? 0x0A : CO2ChangeReporting == "6" ? 0x06 : CO2ChangeReporting == "7" ? 0x07 : CO2ChangeReporting == "8" ? 0x08 : CO2ChangeReporting == "9" ? 0x09 : 0x05)
	cmds << zwave.configurationV1.configurationSet(configurationValue:  LedIndikation == "0" ? [0x00] : [0x01], 																																																																																															parameterNumber:8, size:1, scaledConfigurationValue:  LedIndikation == "0" ? 0x00 : 0x01)
	cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     //get temperature
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x05, scale: 0)     	//get humidity
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     //get dewpoint
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x11, scale: 0)     	//get carbon dioxide
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x27, scale: 1)     	//get voc    
	cmds << zwave.configurationV1.configurationGet(parameterNumber:1)               //get pamam - 1..8
	cmds << zwave.configurationV1.configurationGet(parameterNumber:2)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:3)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:4)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:5)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:6)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:7)
	cmds << zwave.configurationV1.configurationGet(parameterNumber:8)	
    cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet()                  
	cmds << zwave.versionV2.versionGet()
    cmds << zwave.notificationV8.notificationGet(notificationType:13)
	sendEvent(name: "configuration", value: "sent", displayed: true)
	secureSequence(cmds)
}

def updated() {
	def chck = 0
    switch (Checking) {        
        case "1":
            chck = 1*60
            break;
        case "2":
            chck = 2*60
            break;        
        case "3":
            chck = 3*60
            break;        
        case "4":
            chck = 4*60
            break;        
        case "5":
            chck = 5*60
            break;        
        case "10":
            chck = 10*60
            break;        
        case "15":
            chck = 15*60
            break;        
        case "30":
            chck = 30*60
            break;        
        case "60":
            chck = 60*60
            break;
        default:
            chck = 24*60*60        
    }
    sendEvent(name: "checkInterval", value: chck, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
	unschedule(ping)	
}

def ping() { 
	def cmds = []
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     	//get temperature
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x05, scale: 0)     	    //get humidity
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     	//get dewpoint
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x11, scale: 0)     	    //get carbon dioxide
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x27, scale: 1)			//get voc
	secureSequence (cmds)
}

def poll() { //total unklar was Polling macht!!!
	/*def cmds = []
	def tmpE = Temperatureeinheit=="1" ? 1 : 0
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x01, scale: tmpE)     	//get temperature
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x05, scale: 0)     	    //get humidity
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x0B, scale: tmpE)     	//get dewpoint
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x11, scale: 0)     	    //get carbon dioxide
    cmds << zwave.sensorMultilevelV10.sensorMultilevelGet(sensorType:0x27, scale: 1)			//get voc
	secureSequence (cmds)*/
}
