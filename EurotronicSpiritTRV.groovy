metadata {
	definition (name: "Eurotronic Spirit TRV", namespace: "aelfot", author: "Ravil Rubashkin") {
		capability "Battery"
		capability "Lock"
		capability "Thermostat"
		capability "Actuator"
		capability "Sensor"
		capability "TemperatureMeasurement"
		capability "Polling"
		capability "SwitchLevel"	//otherwise, you cannot change the position of the damper through the dashboard

		attribute "Notifity",			"string"
		attribute "DeviceResetLocally",	"bool"

		fingerprint  mfr:"0148", prod:"0003", deviceId:"0001", inClusters:"0x5E,0x55,0x98,0x9F" 
	}
	def batteriestatus = [:]
		batteriestatus << ["0" : "Eventgesteuert"]
		batteriestatus << ["1" : "1 Mal täglich"]

	def windowDetectOptions = [:]
		windowDetectOptions << ["0" : "Deaktiviert"]
		windowDetectOptions << ["1" : "Empfindlichkeit niedrig"]
		windowDetectOptions << ["2" : "Empfindlichkeit mittel"]
		windowDetectOptions << ["3" : "Empfindlichkeit hoch"]

	preferences {
		input name:"parameter1",	type:"bool",	title: "Display invertieren?",				description: "Default: Nein",					defaultValue:false
		input name:"parameter2",	type:"number",	title: "Display ausschalten nach",			description: "Default: Immer an(0)",			defaultValue:0,		range: "0..30"
		input name:"parameter3",	type:"bool",	title: "Hitergrundbeleuchtung",				description: "Default: Deaktiviert",			defaultValue:false
		input name:"parameter4",	type:"enum",	title: "Batteryabfrage",					description: "Default: 1 mal täglich",			defaultValue:"1",	options: batteriestatus
		input name:"parameter5",	type:"number",	title: "Meldung bei Temperaturdifferenz",	description: "Default: bei Delta 0.5°", 		defaultValue:0.5,	range: "0.0..5.0"
		input name:"parameter6",	type:"number",	title: "Meldung bei Valvedifferenz",		description: "Default: Deaktiviert",			defaultValue:0,		range: "0..100"
		input name:"parameter7",	type:"enum",	title: "Fensteroffnungserkennung",			description: "Default: Empfindlichkeit mittel",	defaultValue:"2",	options: windowDetectOptions
		input name:"parameter8",	type:"number",	title: "Temperature offset",				description: "Default: Keine Korrektur",		defaultValue:0,		range: "-5.0..5.0"
		input name:"parameter9",	type:"bool",	title: "Temperatur extern bereitgestellt?",	description: "",								defaultValue:false
		input name:"lg",			type:"bool",	title: "Logging on/off",					description: "",								defaultValue:false
	}
}

def parse(String description) {
	def cmd = zwave.parse(description, getCommandClassVersion())
	if (cmd) {        
		return zwaveEvent(cmd)
    } else {
		log.debug "Non-parsed event: ${description}"
    }
}

def zwaveEvent (hubitat.zwave.commands.notificationv8.NotificationReport cmd) {
	def resultat = [:]
	resultat.name = "Notifity"
	resultat.displayed = true
	switch (cmd.notificationType) {
		case 8:
		if (cmd.eventParameter == []) 	{resultat.value = "Batterie gewechselt"}
		if (cmd.eventParameter == [10]) {resultat.value = "25% Batterie verbleibend"}
		if (cmd.eventParameter == [11]) {resultat.value = "15% Batterie verbleibend"}
		break;
		case 9:
		if (cmd.eventParameter == []) {resultat.value = "Der Fehler wurde gerade behoben"}
		if (cmd.eventParameter == [1]) {resultat.value = "Kein Schließpunkt gefunden"}
		if (cmd.eventParameter == [2]) {resultat.value = "Keine Ventilbewegung möglich"}
		if (cmd.eventParameter == [3]) {resultat.value = "Kein Ventilschließpunkt gefunden"}
		if (cmd.eventParameter == [4]) {resultat.value = "Positionierung fehlgeschlagen"}
		break;
	}
}

def zwaveEvent (hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointReport cmd) {
	def resultat = [:]
	resultat.value = cmd.scaledValue
	resultat.unit = getTemperatureScale()
	resultat.displayed = true
	if (cmd.setpointType == 0x01) { 
		resultat.name = "heatingSetpoint"		
	}
	if (cmd.setpointType == 0x0B) { 
		resultat.name = "coolingSetpoint"		
	}
	sendEvent(resultat)	
}

def zwaveEvent (hubitat.zwave.commands.batteryv1.BatteryReport cmd) {
	sendEvent(name:"battery", value: cmd.batteryLevel)
}

def zwaveEvent(hubitat.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
	sendEvent(name:"DeviceResetLocally", value: true, displayed = true)
}

def zwaveEvent (hubitat.zwave.commands.protectionv1.ProtectionReport cmd) {
	def resultat = [:]
	resultat.name = "lock"
	resultat.displayed = true
	switch (cmd.protectionState) {
		case 0:
		resultat.value = "unlocked"
		break;
		case 1:
		resultat.value = "locked"
		break;
		case 2:
		resultat.value = "unknown"
		break;
	}
	if (resultat.value != null) {sendEvent(resultat)} 
}

def zwaveEvent (hubitat.zwave.commands.thermostatmodev3.ThermostatModeReport cmd) {
	def resultat = [:]
	resultat.name = "thermostatMode"
	resultat.displayed = true
	switch (cmd.mode) {
		case 0:
		resultat.value = "off"
		break;
		case 1:
		resultat.value = "heat"
		break;
		case 11:
		resultat.value = "cool"
		break;
		case 15:
		resultat.value = "emergency heat"
		break;
		case 31:
		resultat.value = "off"
		break;
	}
	sendEvent(resultat)
	if (resultat.value == "emergency heat") {
		runIn(300, "fanOn")
	}
}

def zwaveEvent (hubitat.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd) {
	def resultat = [:]
	resultat.value = cmd.scaledSensorValue
	resultat.name = "temperature"
	resultat.unit = cmd.scale == 1 ? "°F" : "°C"
	resultat.displayed = true
	sendEvent(resultat)
}

def zwaveEvent (hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
	def valvePosition = cmd.value
	def resultat = [:]
	resultat.name = "thermostatOperatingState"
	resultat.displayed = true
	if (valvePosition == 0) {
		resultat.value = "idle"
	} else if (valvePosition < 10) {
		resultat.value = "cooling"
	} else {
		resultat.value = "heating"
	}
	sendEvent(resultat)
	sendEvent(name:"level", value: valvePosition, unit:"%", displayed: true)
}

def zwaveEvent(hubitat.zwave.Command cmd) {
	log.debug "${device.displayName}: Unhandled: $cmd"
}

def off() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x1F).format()
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet().format()
	cmds << new hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelSet(value:0).format()
	cmds << new hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelGet().format()
	sendToDevice(cmds)
}

def heat() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x01).format()
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet().format()	
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x01).format()
	sendToDevice(cmds)
}

def cool() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x0B).format()
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet().format()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x0B).format()
	sendToDevice(cmds)
}

def emergencyHeat() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x0F).format()
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet().format()
	sendToDevice(cmds)
}

def auto() {
}

def fanAuto() {
}

def fanCirculate() {
	log.info "convection mode works regardless of whether you turn it on or not"
	sendEvent(name:"thermostatFanMode", value:"circulate")
}

def fanOn() {
	sendToDevice(new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet().format())
}

def setLevel(nextLevel) {
	def val = 0
	try {
		val = nextLevel.toInteger()
	} catch (e) {
		val = 0
	}
	if (val < 0) {val = 0}
	if (val > 100) {val = 100}
	def cmds = []
	if (device.currentValue("thermostatMode") == "off") {
    	cmds << new hubitat.zwave.commands.switchmultilevelv3.SwitchMultilevelSet(value: val).format()
	}
	cmds << new hubitat.zwave.commands.switchmultilevelv3.SwitchMultilevelGet().format()
	sendToDevice(cmds)
}

def setCoolingSetpoint(temperature) {
	def currentTemperature = device.currentValue("coolingSetpoint").toFloat()
	def nextTemperature = temperature.toFloat()
	if ( Math.abs(currentTemperature - nextTemperature) < 0.5 ) {
		nextTemperature = getTemperature (temperature.toFloat(),currentTemperature)
	} else {
		nextTemperature = getTemperature (temperature.toFloat())
	}
	sendEvent(name: "coolingSetpoint", value: nextTemperature)
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointSet(precision:1, scale:0, scaledValue: nextTemperature, setpointType: 0x0B).format()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x0B).format()
	sendToDevice(cmds)
}

def setHeatingSetpoint(temperature) {
	def currentTemperature = device.currentValue("heatingSetpoint").toFloat()
	def nextTemperature = temperature.toFloat()
	if ( Math.abs(currentTemperature - nextTemperature) < 0.5 ) {
		nextTemperature = getTemperature (temperature.toFloat(),currentTemperature)
	} else {
		nextTemperature = getTemperature (temperature.toFloat())
	}
	sendEvent(name: "heatingSetpoint", value: nextTemperature)
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointSet(precision:1, scale:0, scaledValue: nextTemperature, setpointType: 0x01).format()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x01).format()
	sendToDevice(cmds)
}

def setThermostatFanMode(fanmode) {
	fanCirculate()
}

def setThermostatMode(thermostatmode) {
	switch (thermostatmode) {
		case "emergency heat":
		emergencyHeat()
		break;
		case "cool":
		cool()
		break;
		case "heat":
        heat()
		break;
        case "off":
		off()
		break;
		case "auto":
		auto()
		break;
	}
}

def lock() {
	def cmds = []
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionSet(protectionState:0x01).format()
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionGet().format()
	sendToDevice(cmds)
}

def unlock() {
	def cmds = []
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionSet(protectionState:0x00).format()
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionGet().format()
	sendToDevice(cmds)
}

private getCommandClassVersion() {
	[0x85:2,	//Association
	 0x59:1,	//Association Group Information
	 0x20:1,	//Basic
	 0x80:1,	//Battery
	 0x70:1,	//Configuration
	 0x5A:1,	//Device Reset Locally
	 0x72:1,	//Manufacturer Specific
	 0x31:5,	//Multilevel Sensor
	 0x26:1,	//Multilevel Switch
	 0x71:8,	//Notifikation
	 0x73:1,	//Power Level
	 0x75:1,	//Protection
	 0x98:2,	//Security
	 0x40:3,	//Thermostat Mode
	 0x43:3,	//Thermostat Setpoint
	 0x55:2,	//Transport Service
	 0x86:2,	//Version
	 0x5E:1]	//Z-Wave Plus Info
}

private getTemperature(temp) {
	def Integer initial = Math.round(temp * 10)
	def Integer differenz = initial % 5
	def BigDecimal resultat = initial - differenz
	if (differenz >= 3) {
		resultat = resultat + 5
	}
	resultat = resultat / 10
	if (resultat < 8) {resultat = 8}
	if (resultat > 28){reulstat = 28}
	return resultat
}

private getTemperature(tempN,tempC) {
	if (tempN > tempC) {
		tempN = tempC + 0.5
		if (tempN >= 28) {
			tempN = 28
		}
	}
	if (tempN < tempC) {
		tempN = tempC - 0.5
		if (tempN <= 8) {
			tempN = 8
		}
	}
	return tempN
}

void poll() {
	def cmds = []
	cmds << new hubitat.zwave.commands.switchmultilevelv3.SwitchMultilevelGet().format()
	cmds << new hubitat.zwave.commands.sensormultilevelv5.SensorMultilevelGet(sensorType:1).format()
	cmds << new hubitat.zwave.commands.batteryv1.BatteryGet().format()
	sendToDevice(cmds)
}

void updated() {
	def cmds = []
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:1,	size:1,	scaledConfigurationValue: parameter1 ? 0x01 : 0x00).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:2,	size:1,	scaledConfigurationValue: Math.round(parameter2.toFloat())).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:3,	size:1,	scaledConfigurationValue: parameter3 ? 0x01 : 0x00).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:4,	size:1,	scaledConfigurationValue: parameter4.toInteger()).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:5,	size:1,	scaledConfigurationValue: Math.round(parameter5.toFloat() * 10)).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:6,	size:1,	scaledConfigurationValue: Math.round(parameter6.toFloat())).format()
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:7,	size:1,	scaledConfigurationValue: parameter7.toInteger()).format()
	if (parameter9) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: -128).format()
	} else {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: Math.round(parameter8.toFloat() * 10)).format()
	}
	if (device.currentValue("DeviceResetLocally") == null) {installed()}
	sendToDevice(cmds)
}

void installed() {
	sendEvent(name:"Notifity",						value:"Installed")
	sendEvent(name:"DeviceResetLocally",			value:false)
	sendEvent(name:"supportedThermostatFanModes", 	value: ["circulate"])
	sendEvent(name:"supportedThermostatModes",		value: ["off", "heat", "emergency heat", "cool"])
	def cmds = []
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionGet().format()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x01).format()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x0B).format()
	cmds << new hubitat.zwave.commands.batteryv1.BatteryGet().format()
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet().format()
	cmds << new hubitat.zwave.commands.sensormultilevelv5.SensorMultilevelGet(sensorType:1).format()
	sendToDevice(cmds)
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
