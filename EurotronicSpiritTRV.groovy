import groovy.transform.Field
import hubitat.helper.HexUtils
import hubitat.device.HubAction

metadata {
	definition (name: "Eurotronic Spirit TRV", namespace: "aelfot", author: "Ravil Rubashkin") {
		capability "Battery"
		capability "Lock"
		capability "Thermostat"
		capability "Actuator"
		capability "Sensor"
		capability "TemperatureMeasurement"
		capability "Polling"
		capability "SwitchLevel"

		attribute "ExterneTemperatur", "string"
		attribute "Notifity",			"string"
		attribute "DeviceResetLocally",	"bool"

		command "testNode",		[[name: "Power Level", type: "NUMBER", description: "Power 0-9"],
													 [name: "Test Frame Count", type: "NUMBER", description: ""],
													 [name: "Node ID", type: "STRING", description: ""]]
		command "manual"
		command "lokaleBedinungDeaktiviert"

		fingerprint  mfr:"0148", prod:"0003", deviceId:"0001", inClusters:"0x5E,0x55,0x98,0x9F"
	}
	def batteriestatus =  [:]
		batteriestatus << [0 : "Eventgesteuert"]
		batteriestatus << [1 : "1 Mal täglich"]

	def windowDetectOptions =  [:]
		windowDetectOptions << [0 : "Deaktiviert"]
		windowDetectOptions << [1 : "Empfindlichkeit niedrig"]
		windowDetectOptions << [2 : "Empfindlichkeit mittel"]
		windowDetectOptions << [3 : "Empfindlichkeit hoch"]

	preferences {
		input name:"parameter1",	type:"bool",	title: "Display invertieren?",				description: "Default: Nein",					defaultValue:false
		input name:"parameter2",	type:"number",	title: "Display ausschalten nach",			description: "Default: Immer an(0)",			defaultValue:0,		range: "0..30"
		input name:"parameter3",	type:"bool",	title: "Hintergrundbeleuchtung",			description: "Default: Deaktiviert",			defaultValue:false
		input name:"parameter4",	type:"enum",	title: "Batteryabfrage",					description: "Default: 1 mal täglich",			defaultValue:1,		options: batteriestatus
		input name:"parameter5",	type:"number",	title: "Meldung bei Temperaturdifferenz",	description: "Default: bei Delta 0.5°", 		defaultValue:0.5,	range: "0.0..5.0"
		input name:"parameter6",	type:"number",	title: "Meldung bei Valvedifferenz",		description: "Default: Deaktiviert",			defaultValue:0,		range: "0..100"
		input name:"parameter7",	type:"enum",	title: "Fensteroffnungserkennung",			description: "Default: Empfindlichkeit mittel",	defaultValue:2,		options: windowDetectOptions
		input name:"parameter8",	type:"number",	title: "Temperature offset",				description: "Default: Keine Korrektur",		defaultValue:0,		range: "-5.0..5.0"
		input name:"parameter9",	type:"bool",	title: "Temperatur extern bereitgestellt?",	description: "",								defaultValue:false
		input name:"lg",			type:"bool",	title: "Logging on/off",					description: "",								defaultValue:false
	}
}

@Field static Map commandClassVersions =
	[0x85:2,	//Association
	 0x59:1,	//Association Group Information
	 0x20:1,	//Basic
	 0x80:1,	//Battery
	 0x70:1,	//Configuration
	 0x5A:1,	//Device Reset Locally
	 0x7A:3,	//Firmware Update Md V3
	 0x72:1,	//Manufacturer Specific
	 0x31:5,	//Multilevel Sensor
	 0x26:1,	//Multilevel Switch
	 0x71:8,	//Notifikation
	 0x73:1,	//Power Level
	 0x75:1,	//Protection
	 0x98:2,	//Security ohne verschlüsselung
	 0x40:3,	//Thermostat Mode
	 0x43:3,	//Thermostat Setpoint
	 0x55:2,	//Transport Service ohne verschlüsselung
	 0x86:2,	//Version
	 0x5E:2]	//Z-Wave Plus Info ohne verschlüsselung

def parse(String description) {
	def cmd = zwave.parse(description, commandClassVersions)
	if (cmd) {
		return zwaveEvent(cmd)
	} else {
		log.debug "Non-parsed event: ${description}"
	}
}

void zwaveEvent (hubitat.zwave.commands.notificationv8.NotificationReport cmd) {
	def resultat = [:]
	resultat.name = "Notifity"
	resultat.displayed = true
	switch (cmd.notificationType) {
		case 0x08:
		if (cmd.event == 0x0A) {
			resultat.value = "25% Batterie verbleibend"
		} else if (cmd.event == 0x0B) {
			resultat.value = "15% Batterie verbleibend"
		} else {
			resultat.value = "Batterie gewechselt"
		}
		break;
		case 0x09:
		if (cmd.event == 0x03) {
			if (cmd.eventParametersLength != 0) {
				switch (cmd.eventParameter[0]) {
					case 0x01:
					resultat.value = "Kein Schließpunkt gefunden"
					break;
					case 0x02:
					resultat.value = "Keine Ventilbewegung möglich"
					break;
					case 0x03:
					resultat.value = "Kein Ventilschließpunkt gefunden"
					break;
					case 0x04:
					resultat.value = "Positionierung fehlgeschlagen"
					break;
				}
			} else {
				resultat.value = "Der Fehler wurde gerade behoben"
			}
		}
		break;
	}
	if (lg) log.info"Notifikaiton ist ${cmd}"
	sendEvent(resultat)
}

void zwaveEvent (hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointReport cmd) {
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
	if (lg) log.info "Thermostat hat den Report ${cmd}"
	sendEvent(resultat)
}

void zwaveEvent (hubitat.zwave.commands.batteryv1.BatteryReport cmd) {
	if (lg) log.info "batteryreport ist ${cmd}"
	sendEvent(name:"battery", value: cmd.batteryLevel, displayed: true)
}

void zwaveEvent(hubitat.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
	sendEvent(name:"DeviceResetLocally", value: true, displayed = true)
}

void zwaveEvent (hubitat.zwave.commands.protectionv1.ProtectionReport cmd) {
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
		resultat.value = "lokale Bedinung deaktiviert" //"unknown"
		break;
	}
	if (resultat.value != null) {sendEvent(resultat)}
	if (lg) log.info "protection report ist ${cmd}"
}

void zwaveEvent (hubitat.zwave.commands.thermostatmodev3.ThermostatModeReport cmd) {
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
		resultat.value = "manual"
		break;
	}
	sendEvent(resultat)
	if (lg) log.info "thermostat hat den mode gemeldet ${cmd}"
}

void zwaveEvent (hubitat.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd) {
	def resultat = [:]
	resultat.value = cmd.scaledSensorValue
	resultat.name = "temperature"
	resultat.unit = cmd.scale == 1 ? "°F" : "°C"
	resultat.displayed = true
	if (lg) log.info "temperature ist ${cmd}"
	sendEvent(resultat)
}

void zwaveEvent (hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
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
	if (lg) log.info "Valveposition is ${valvePosition}"
	sendEvent(name:"level", value: valvePosition)
	sendEvent(resultat)
}

void zwaveEvent(hubitat.zwave.Command cmd) {
	log.debug "${device.displayName}: Unhandled: $cmd"
}

void off() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x00)
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet()
	sendToDevice(cmds)
}

void heat() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x01)
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x01)
	sendToDevice(cmds)
}

void cool() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x0B)
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x0B)
	sendToDevice(cmds)
}

void emergencyHeat() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x0F)
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet()
	sendToDevice(cmds)
}

void manual() {
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeSet(mode:0x1F)
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet()
	cmds << new hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelSet(value: 0)
	cmds << new hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelGet()
	sendToDevice(cmds)
}

void zwaveEvent (hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {
	def cmds = []
	switch (cmd.parameterNumber) {
		case 1:
		if ((parameter1 ? 0x01 : 0x00) != cmd.scaledConfigurationValue) {
			if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:1,	size:1,	scaledConfigurationValue: parameter1 ? 0x01 : 0x00)
		} else {
			if (lg) log.info "Parameter nummer  ${cmd.parameterNumber} hat den Wert erfolgreich übernommen"
		}
		break;
		case 2:
		if (Math.round(parameter2).toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:2,	size:1,	scaledConfigurationValue: Math.round(parameter2).toInteger())
		} else {
			if (lg) log.info "Parameter nummer  ${cmd.parameterNumber} hat den Wert erfolgreich übernommen"
		}
		break;
		case 3:
		if ((parameter3 ? 0x01 : 0x00) != cmd.scaledConfigurationValue) {
			if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:3,	size:1,	scaledConfigurationValue: parameter3 ? 0x01 : 0x00)
		} else {
			if (lg) log.info "Parameter nummer  ${cmd.parameterNumber} hat den Wert erfolgreich übernommen"
		}
		break;
		case 4:
		if (parameter4.toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:4,	size:1,	scaledConfigurationValue: parameter4.toInteger())
		} else {
			if (lg) log.info "Parameter nummer  ${cmd.parameterNumber} hat den Wert erfolgreich übernommen"
		}
		break;
		case 5:
		if (Math.round(parameter5.toFloat() * 10).toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:5,	size:1,	scaledConfigurationValue: Math.round(parameter5.toFloat() * 10).toInteger())
		} else {
			if (lg) log.info "Parameter nummer  ${cmd.parameterNumber} hat den Wert erfolgreich übernommen"
		}
		break;
		case 6:
		if (Math.round(parameter6).toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:6,	size:1,	scaledConfigurationValue: Math.round(parameter6).toInteger())
		} else {
			if (lg) log.info "Parameter nummer  ${cmd.parameterNumber} hat den Wert erfolgreich übernommen"
		}
		break;
		case 7:
		if (parameter7.toInteger() != cmd.scaledConfigurationValue) {
			if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
			cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:7,	size:1,	scaledConfigurationValue: parameter7.toInteger())
		} else {
			if (lg) log.info "Parameter nummer  ${cmd.parameterNumber} hat den Wert erfolgreich übernommen"
		}
		break;
		case 8:
		if (parameter9) {
			if (cmd.scaledConfigurationValue != -128) {
				if (lg) log.info "Parameter nummer 9 hat den Wert nich übernommen, erneter Versuch"
				cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: -128)
			} else {
				if (lg) log.info "Parameter nummer 8 hat den Wert erfolgreich übernommen"
				if (lg) log.info "Parameter nummer 9 hat den Wert erfolgreich übernommen"
				sendEvent (name: "ExterneTemperatur", value: "true")
			}
		} else  {
			if (cmd.scaledConfigurationValue != Math.round(parameter8.toFloat() * 10).toInteger()) {
				if (lg) log.info "Parameter nummer ${cmd.parameterNumber} hat den Wert nich übernommen, erneter Versuch"
				cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: Math.round(parameter8.toFloat() * 10).toInteger())
			} else {
				if (lg) log.info "Parameter nummer 8 hat den Wert erfolgreich übernommen"
				if (lg) log.info "Parameter nummer 9 hat den Wert erfolgreich übernommen"
				sendEvent (name: "ExterneTemperatur", value: "false")
			}
		}
		break;
	}
	if (cmds != []) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationGet(parameterNumber:cmd.parameterNumber)
		sendToDevice(cmds)
	}
}

void setLevel(nextLevel) {
	def val = 0
	try {
		val = nextLevel.toInteger()
	} catch (e) {
		val = 0
	}
	if (val < 0) {val = 0}
	if (val > 100) {val = 100}
	if (device.currentValue("thermostatMode") == "manual") {
		def cmds = []
		cmds << new hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelSet(value: val)
		cmds << new hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelGet()
		sendToDevice(cmds)
		if (lg) log.info "Die Valve wird auf den Wert ${val} gestellt"
	} else {
		sendToDevice(new hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelGet())
		if (lg) log.info "Eine Einstellung der Valveöffnung ist nur im manual modus möglich"
	}
}

void setCoolingSetpoint(temperature) {
	def nextTemperature = getTemperature (temperature,"cool")
	sendEvent(name: "coolingSetpoint", value: nextTemperature.toFloat(), displayed: true)
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointSet(precision:1, scale:0, scaledValue: nextTemperature, setpointType: 0x0B)
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x0B)
	sendToDevice(cmds)
}

void setHeatingSetpoint(temperature) {
	def nextTemperature = getTemperature (temperature,"heat")
	sendEvent(name: "heatingSetpoint", value: nextTemperature.toFloat(), displayed: true)
	def cmds = []
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointSet(precision:1, scale:0, scaledValue: nextTemperature, setpointType: 0x01)
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x01)
	sendToDevice(cmds)
}

private getTemperature (setTemperature, modus) {
	def currentTemperature = 0
	def BigDecimal nextTemperature = 0
	if (modus == "cool") {
		currentTemperature = device.currentValue("coolingSetpoint").toFloat()
	} else {
		currentTemperature = device.currentValue("heatingSetpoint").toFloat()
	}
	if ( Math.abs(currentTemperature - setTemperature) < 0.5 ) {
		if (setTemperature > currentTemperature) {
			nextTemperature = currentTemperature + 0.5
			if (setTemperature >= 28) {
				nextTemperature = 28.0
			}
		}
		if (setTemperature < currentTemperature) {
			nextTemperature = currentTemperature - 0.5
			if (nextTemperature <= 8) {
				nextTemperature = 8.0
			}
		}
		if (setTemperature == currentTemperature) {
			nextTemperature = setTemperature
		}
	} else {
		def Integer temp = Math.round(setTemperature * 10)
		def Integer modul = temp % 5
		nextTemperature = temp - modul
		if (modul >= 3) {
			nextTemperature = nextTemperature + 5
		}
		nextTemperature = nextTemperature / 10
		if (nextTemperature < 8) {nextTemperature = 8}
		if (nextTemperature > 28){nextTemperature = 28}
	}
	return nextTemperature
}

void setThermostatMode(thermostatmode) {
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
		case "manual":
		manual()
		break;
	}
}

void lock() {
	def cmds = []
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionSet(protectionState:0x01)
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionGet()
	sendToDevice(cmds)
}

void unlock() {
	def cmds = []
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionSet(protectionState:0x00)
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionGet()
	sendToDevice(cmds)
}

void lokaleBedinungDeaktiviert () {
	def cmds = []
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionSet(protectionState:0x02)
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionGet()
	sendToDevice(cmds)
}

void poll() {
	def cmds = []
	cmds << new hubitat.zwave.commands.switchmultilevelv3.SwitchMultilevelGet()
	cmds << new hubitat.zwave.commands.sensormultilevelv5.SensorMultilevelGet(sensorType:1)
	cmds << new hubitat.zwave.commands.batteryv1.BatteryGet()
	sendToDevice(cmds)
	if (lg) log.info "Polling"
}

void updated() {
	def cmds = []
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:1,	size:1,	scaledConfigurationValue: parameter1 ? 0x01 : 0x00)
	if (lg) log.info "Parameter 1 hat den Wert ${parameter1 ? 0x01 : 0x00} übermittelt bekommen"
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:2,	size:1,	scaledConfigurationValue: Math.round(parameter2.toFloat()).toInteger())
	if (lg) log.info "Parameter 2 hat den Wert ${Math.round(parameter2.toFloat()).toInteger()} übermittelt bekommen"
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:3,	size:1,	scaledConfigurationValue: parameter3 ? 0x01 : 0x00)
	if (lg) log.info "Parameter 3 hat den Wert ${parameter3 ? 0x01 : 0x00} übermittelt bekommen"
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:4,	size:1,	scaledConfigurationValue: parameter4.toInteger())
	if (lg) log.info "Parameter 4 hat den Wert ${parameter4.toInteger()} übermittelt bekommen"
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:5,	size:1,	scaledConfigurationValue: Math.round(parameter5.toFloat() * 10).toInteger())
	if (lg) log.info "Parameter 5 hat den Wert ${Math.round(parameter5.toFloat() * 10).toInteger()} übermittelt bekommen"
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:6,	size:1,	scaledConfigurationValue: Math.round(parameter6.toFloat()).toInteger())
	if (lg) log.info "Parameter 6 hat den Wert ${Math.round(parameter6.toFloat()).toInteger()} übermittelt bekommen"
	cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:7,	size:1,	scaledConfigurationValue: parameter7.toInteger())
	if (lg) log.info "Parameter 7 hat den Wert ${parameter7.toInteger()} übermittelt bekommen"
	if (parameter9) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: -128)
		if (lg) log.info "Parameter 8 hat den Wert -128 übermittelt bekommen"
	} else {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber:8,	size:1,	scaledConfigurationValue: Math.round(parameter8.toFloat() * 10).toInteger())
		if (lg) log.info "Parameter 8 hat den Wert ${Math.round(parameter8.toFloat() * 10).toInteger()} übermittelt bekommen"
	}
	for (int i=1 ; i<=8 ; i++) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationGet(parameterNumber: i)
	}
	sendToDevice(cmds)
}

void installed() {
	sendEvent(name:"Notifity",						value:"Installed", displayed: true)
	sendEvent(name:"DeviceResetLocally",			value:false, displayed: true)
	sendEvent(name:"supportedThermostatFanModes", 	value: ["circulate"], displayed: true)
	sendEvent(name:"supportedThermostatModes",		value: ["off", "heat", "emergency heat", "cool", "manual"], displayed: true)
	def cmds = []
	cmds << new hubitat.zwave.commands.protectionv1.ProtectionGet()
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x01)
	cmds << new hubitat.zwave.commands.thermostatsetpointv3.ThermostatSetpointGet(setpointType:0x0B)
	cmds << new hubitat.zwave.commands.batteryv1.BatteryGet()
	cmds << new hubitat.zwave.commands.thermostatmodev3.ThermostatModeGet()
	cmds << new hubitat.zwave.commands.sensormultilevelv5.SensorMultilevelGet(sensorType:1)
	for (int i=1 ; i<=8 ; i++) {
		cmds << new hubitat.zwave.commands.configurationv1.ConfigurationSet(parameterNumber: i, defaultValue: true)
	}
	sendToDevice(cmds)
}

void sendToDevice(List<hubitat.zwave.Command> cmds, Long delay=1000) {
	sendHubCommand(new hubitat.device.HubMultiAction(commands(cmds, delay), hubitat.device.Protocol.ZWAVE))
}

void sendToDevice(hubitat.zwave.Command cmd, Long delay=1000) {
	sendHubCommand(new hubitat.device.HubAction(zwaveSecureEncap(cmd.format()), hubitat.device.Protocol.ZWAVE))
}

List<String> commands(List<hubitat.zwave.Command> cmds, Long delay=1000) {
	return delayBetween(cmds.collect{ zwaveSecureEncap(it.format()) }, delay)
}

void fanOn() {
}

void auto() {
}

void fanAuto() {
}

void fanCirculate() {	
}

void setThermostatFanMode(fanmode) {
	sendEvent(name: "thermostatFanMode", value: "circulate", displayed: true)
}

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
