/*
Version 1.03

ACHTUNG!! 
	Lediglich ThermostatModeV2 von Hubitat unterstützt. Es fehlen Modi: Manufacturer Specific und Full Power
	Lediglich ThermostatSetPointV2 von Hubitat unterstützt. Keine Nachteile.
	Lediglich SecurityV1 von Hubitat unterstützt.
	ZWavePlusInfoV2 in Hubitat.
    
Bedienung!
	auto => heat
	cool => energy save heat
	off => off
	emergency heat => full power
	heat => manufacturer specific

ToDo!
	Externe Temperaturmessung: Assotiation 

	firmware update metadata v3: macht Sinn??? Wo Firmware??
	power level v1: wozu mit Testfallen spielen??? keine Zeit dafür momentan
	transport service v2 u: soll es sein???
*/


metadata {
	definition (name: "Eurotronic Spirit", namespace: "aelfot", author: "Ravil Rubashkin") {
		capability "Configuration"
		capability "Battery"
		capability "Lock"														//Kindersicherung
		capability "SwitchLevel"												//Ventilkontrolle
		capability "Thermostat"
		capability "TamperAlert"												//Lokaler Reset
		capability "Initialize"
		
		attribute "Notifity", "string"
				
		fingerprint mfr: "0148", prod: "0003", model: "0001", deviceJoinName: "Eurotronic Spirit TRV"
	}
	def LCDinvertOptions = [:]
		LCDinvertOptions << ["0" : "Ja"]												//0x00
		LCDinvertOptions << ["1" : "Nein"]												//0x01

	def LCDtimeoutOptions = [:]
		LCDtimeoutOptions << ["0"  : "Immer an"]														//0x00
		LCDtimeoutOptions << ["1"  : "1 Secunde"]														//0x01
		LCDtimeoutOptions << ["2"  : "2 Secunden"]														//0x02
		LCDtimeoutOptions << ["3"  : "3 Secunden"]														//0x03
		LCDtimeoutOptions << ["4"  : "4 Secunden"]														//0x04
		LCDtimeoutOptions << ["6"  : "6 Secunden"]														//0x06
		LCDtimeoutOptions << ["7"  : "7 Secunden"]														//0x07
 		LCDtimeoutOptions << ["8"  : "8 Secunden"]														//0x08
		LCDtimeoutOptions << ["9"  : "9 Secunden"]														//0x09
		LCDtimeoutOptions << ["10" : "10 Secunden"]														//0x0A
		LCDtimeoutOptions << ["11" : "11 Secunden"]														//0x0B
       	LCDtimeoutOptions << ["12" : "12 Secunden"]														//0x0C
       	LCDtimeoutOptions << ["13" : "13 Secunden"]														//0x0D
		LCDtimeoutOptions << ["14" : "14 Secunden"]														//0x0E
		LCDtimeoutOptions << ["15" : "15 Secunden"]														//0x0F
		LCDtimeoutOptions << ["16" : "16 Secunden"]														//0x10
		LCDtimeoutOptions << ["17" : "17 Secunden"]														//0x11
		LCDtimeoutOptions << ["18" : "18 Secunden"]														//0x12
		LCDtimeoutOptions << ["19" : "19 Secunden"]														//0x13
		LCDtimeoutOptions << ["20" : "20 Secunden"]														//0x14
		LCDtimeoutOptions << ["21" : "21 Secunden"]														//0x15
		LCDtimeoutOptions << ["22" : "22 Secunden"]														//0x16
		LCDtimeoutOptions << ["23" : "23 Secunden"]														//0x17
		LCDtimeoutOptions << ["24" : "24 Secunden"]														//0x18
		LCDtimeoutOptions << ["25" : "25 Secunden"]														//0x19
		LCDtimeoutOptions << ["26" : "26 Secunden"]														//0x1A
		LCDtimeoutOptions << ["27" : "27 Secunden"]														//0x1B
		LCDtimeoutOptions << ["28" : "28 Secunden"]														//0x1C
		LCDtimeoutOptions << ["29" : "29 Secunden"]														//0x1D
		LCDtimeoutOptions << ["30" : "20 Secunden"]														//0x1E
				
	def backlightOptions = [:]
		backlightOptions << ["0" : "An"]																//0x00
		backlightOptions << ["1" : "Aus"]																//0x01

	def batteryNotOptions = [:]
		batteryNotOptions << ["0" : "Eventgesteuert"]													//0x00
		batteryNotOptions << ["1" : "1 Mal täglich"]													//0x01

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

	def valveReportRates = [:] 
		valveReportRates << ["0"  : "Deaktiviert"]														//0x00
		valveReportRates << ["1"  : "Ventilöffnungsgrad bei Delta von 1% melden"]						//0x01
		valveReportRates << ["2"  : "Ventilöffnungsgrad bei Delta von 2% melden"]						//0x02
		valveReportRates << ["3"  : "Ventilöffnungsgrad bei Delta von 3% melden"]						//0x03
		valveReportRates << ["4"  : "Ventilöffnungsgrad bei Delta von 4% melden"]						//0x04
		valveReportRates << ["5"  : "Ventilöffnungsgrad bei Delta von 5% melden"]						//0x05
		valveReportRates << ["6"  : "Ventilöffnungsgrad bei Delta von 6% melden"]						//0x06
		valveReportRates << ["7"  : "Ventilöffnungsgrad bei Delta von 7% melden"]						//0x07
		valveReportRates << ["8"  : "Ventilöffnungsgrad bei Delta von 8% melden"]						//0x08
		valveReportRates << ["9"  : "Ventilöffnungsgrad bei Delta von 9% melden"]						//0x09
		valveReportRates << ["10" : "Ventilöffnungsgrad bei Delta von 10% melden"]						//0x0A
		valveReportRates << ["11" : "Ventilöffnungsgrad bei Delta von 11% melden"]						//0x0B
		valveReportRates << ["12" : "Ventilöffnungsgrad bei Delta von 12% melden"]						//0x0C
		valveReportRates << ["13" : "Ventilöffnungsgrad bei Delta von 13% melden"]						//0x0D
		valveReportRates << ["14" : "Ventilöffnungsgrad bei Delta von 14% melden"]						//0x0E
		valveReportRates << ["15" : "Ventilöffnungsgrad bei Delta von 15% melden"]						//0x0F
		valveReportRates << ["16" : "Ventilöffnungsgrad bei Delta von 16% melden"]						//0x10
		valveReportRates << ["17" : "Ventilöffnungsgrad bei Delta von 17% melden"]						//0x11
		valveReportRates << ["18" : "Ventilöffnungsgrad bei Delta von 18% melden"]						//0x12
		valveReportRates << ["19" : "Ventilöffnungsgrad bei Delta von 19% melden"]						//0x13
		valveReportRates << ["20" : "Ventilöffnungsgrad bei Delta von 20% melden"]						//0x14
		valveReportRates << ["21" : "Ventilöffnungsgrad bei Delta von 21% melden"]						//0x15
		valveReportRates << ["22" : "Ventilöffnungsgrad bei Delta von 22% melden"]						//0x16
		valveReportRates << ["23" : "Ventilöffnungsgrad bei Delta von 23% melden"]						//0x17
		valveReportRates << ["24" : "Ventilöffnungsgrad bei Delta von 24% melden"]						//0x18
		valveReportRates << ["25" : "Ventilöffnungsgrad bei Delta von 25% melden"]						//0x19
		valveReportRates << ["26" : "Ventilöffnungsgrad bei Delta von 26% melden"]						//0x1A
		valveReportRates << ["27" : "Ventilöffnungsgrad bei Delta von 27% melden"]						//0x1B
		valveReportRates << ["28" : "Ventilöffnungsgrad bei Delta von 28% melden"]						//0x1C
		valveReportRates << ["29" : "Ventilöffnungsgrad bei Delta von 29% melden"]						//0x1D
		valveReportRates << ["30" : "Ventilöffnungsgrad bei Delta von 30% melden"]						//0x1E
		valveReportRates << ["31" : "Ventilöffnungsgrad bei Delta von 31% melden"]						//0x1F
		valveReportRates << ["32" : "Ventilöffnungsgrad bei Delta von 32% melden"]						//0x20
		valveReportRates << ["33" : "Ventilöffnungsgrad bei Delta von 33% melden"]						//0x21
		valveReportRates << ["34" : "Ventilöffnungsgrad bei Delta von 34% melden"]						//0x22
		valveReportRates << ["35" : "Ventilöffnungsgrad bei Delta von 35% melden"]						//0x23
		valveReportRates << ["36" : "Ventilöffnungsgrad bei Delta von 36% melden"]						//0x24
		valveReportRates << ["37" : "Ventilöffnungsgrad bei Delta von 37% melden"]						//0x25
		valveReportRates << ["38" : "Ventilöffnungsgrad bei Delta von 38% melden"]						//0x26
		valveReportRates << ["39" : "Ventilöffnungsgrad bei Delta von 39% melden"]						//0x27
		valveReportRates << ["40" : "Ventilöffnungsgrad bei Delta von 40% melden"]						//0x28
		valveReportRates << ["41" : "Ventilöffnungsgrad bei Delta von 41% melden"]						//0x29
		valveReportRates << ["42" : "Ventilöffnungsgrad bei Delta von 42% melden"]						//0x2A
		valveReportRates << ["43" : "Ventilöffnungsgrad bei Delta von 43% melden"]						//0x2B
		valveReportRates << ["44" : "Ventilöffnungsgrad bei Delta von 44% melden"]						//0x2C
		valveReportRates << ["45" : "Ventilöffnungsgrad bei Delta von 45% melden"]						//0x2D
		valveReportRates << ["46" : "Ventilöffnungsgrad bei Delta von 46% melden"]						//0x2E
		valveReportRates << ["47" : "Ventilöffnungsgrad bei Delta von 47% melden"]						//0x2F
		valveReportRates << ["48" : "Ventilöffnungsgrad bei Delta von 48% melden"]						//0x30
		valveReportRates << ["49" : "Ventilöffnungsgrad bei Delta von 49% melden"]						//0x31
		valveReportRates << ["50" : "Ventilöffnungsgrad bei Delta von 50% melden"]						//0x32
    
	def windowDetectOptions = [:]
		windowDetectOptions << ["0" : "Deaktiviert"] 													//0x00
		windowDetectOptions << ["1" : "Empfindlichkeit niedrig"]										//0x01
		windowDetectOptions << ["2" : "Empfindlichkeit mittel"]											//0x02
		windowDetectOptions << ["3" : "Empfindlichkeit hoch"]											//0x03
				
	def tempOffset = [:]
		tempOffset << ["-50" : "Temperaturkorrektur um -5.0°C"]											//0xCE
		tempOffset << ["-49" : "Temperaturkorrektur um -4.9°C"]											//0xCF
		tempOffset << ["-48" : "Temperaturkorrektur um -4.8°C"]											//0xD0
		tempOffset << ["-47" : "Temperaturkorrektur um -4.7°C"]											//0xD1
		tempOffset << ["-46" : "Temperaturkorrektur um -4.6°C"]											//0xD2
		tempOffset << ["-45" : "Temperaturkorrektur um -4.5°C"]											//0xD3
		tempOffset << ["-44" : "Temperaturkorrektur um -4.4°C"]											//0xD4
		tempOffset << ["-43" : "Temperaturkorrektur um -4.3°C"]											//0xD5
		tempOffset << ["-42" : "Temperaturkorrektur um -4.2°C"]											//0xD6
		tempOffset << ["-41" : "Temperaturkorrektur um -4.1°C"]											//0xD7
		tempOffset << ["-40" : "Temperaturkorrektur um -4.0°C"]											//0xD8
		tempOffset << ["-39" : "Temperaturkorrektur um -3.9°C"]											//0xD9
		tempOffset << ["-38" : "Temperaturkorrektur um -3.8°C"]											//0xDA
		tempOffset << ["-37" : "Temperaturkorrektur um -3.7°C"]											//0xDB
		tempOffset << ["-36" : "Temperaturkorrektur um -3.6°C"]											//0xDC
		tempOffset << ["-35" : "Temperaturkorrektur um -3.5°C"]											//0xDD
		tempOffset << ["-34" : "Temperaturkorrektur um -3.4°C"]											//0xDE
		tempOffset << ["-33" : "Temperaturkorrektur um -3.3°C"]											//0xDF
		tempOffset << ["-32" : "Temperaturkorrektur um -3.2°C"]											//0xE0
		tempOffset << ["-31" : "Temperaturkorrektur um -3.1°C"]											//0xE1
		tempOffset << ["-30" : "Temperaturkorrektur um -3.0°C"]											//0xE2
		tempOffset << ["-29" : "Temperaturkorrektur um -2.9°C"]											//0xE3
		tempOffset << ["-28" : "Temperaturkorrektur um -2.8°C"]											//0xE4
		tempOffset << ["-27" : "Temperaturkorrektur um -2.7°C"]											//0xE5
		tempOffset << ["-26" : "Temperaturkorrektur um -2.6°C"]											//0xE6
		tempOffset << ["-25" : "Temperaturkorrektur um -2.5°C"]											//0xE7
		tempOffset << ["-24" : "Temperaturkorrektur um -2.4°C"]											//0xE8
		tempOffset << ["-23" : "Temperaturkorrektur um -2.3°C"]											//0xE9
		tempOffset << ["-22" : "Temperaturkorrektur um -2.2°C"]											//0xEA
		tempOffset << ["-21" : "Temperaturkorrektur um -2.1°C"]											//0xEB
		tempOffset << ["-20" : "Temperaturkorrektur um -2.0°C"]											//0xEC
		tempOffset << ["-19" : "Temperaturkorrektur um -1.9°C"]											//0xED
		tempOffset << ["-18" : "Temperaturkorrektur um -1.8°C"]											//0xEE
		tempOffset << ["-17" : "Temperaturkorrektur um -1.7°C"]											//0xEF
		tempOffset << ["-16" : "Temperaturkorrektur um -1.6°C"]											//0xF0
		tempOffset << ["-15" : "Temperaturkorrektur um -1.5°C"]											//0xF1
		tempOffset << ["-14" : "Temperaturkorrektur um -1.4°C"]											//0xF2
		tempOffset << ["-13" : "Temperaturkorrektur um -1.3°C"]											//0xF3
		tempOffset << ["-12" : "Temperaturkorrektur um -1.2°C"]											//0xF4
		tempOffset << ["-11" : "Temperaturkorrektur um -1.1°C"]											//0xF5
		tempOffset << ["-10" : "Temperaturkorrektur um -1.0°C"]											//0xF6
		tempOffset << ["-9"  : "Temperaturkorrektur um -0.9°C"]											//0xF7
		tempOffset << ["-8"  : "Temperaturkorrektur um -0.8°C"]											//0xF8
		tempOffset << ["-7"  : "Temperaturkorrektur um -0.7°C"]											//0xF9
		tempOffset << ["-6"  : "Temperaturkorrektur um -0.6°C"]											//0xFA
		tempOffset << ["-5"  : "Temperaturkorrektur um -0.5°C"]											//0xFB
		tempOffset << ["-4"  : "Temperaturkorrektur um -0.4°C"]											//0xFC
		tempOffset << ["-3"  : "Temperaturkorrektur um -0.3°C"]											//0xFD
		tempOffset << ["-2"  : "Temperaturkorrektur um -0.2°C"]											//0xFE
		tempOffset << ["-1"  : "Temperaturkorrektur um -0.1°C"]											//0xFF
		tempOffset << ["0"   : "Keine Korrektur"]              											//0x00
		tempOffset << ["1"   : "Temperaturkorrektur um 0.1°C"]											//0x01
		tempOffset << ["2"   : "Temperaturkorrektur um 0.2°C"]											//0x02
		tempOffset << ["3"   : "Temperaturkorrektur um 0.3°C"]											//0x03
		tempOffset << ["4"   : "Temperaturkorrektur um 0.4°C"]											//0x04
		tempOffset << ["5"   : "Temperaturkorrektur um 0.5°C"]											//0x05
		tempOffset << ["6"   : "Temperaturkorrektur um 0.6°C"]											//0x06
		tempOffset << ["7"   : "Temperaturkorrektur um 0.7°C"]											//0x07
		tempOffset << ["8"   : "Temperaturkorrektur um 0.8°C"]											//0x08
		tempOffset << ["9"   : "Temperaturkorrektur um 0.9°C"]											//0x09
		tempOffset << ["10"  : "Temperaturkorrektur um 1.0°C"]											//0x0A
		tempOffset << ["11"  : "Temperaturkorrektur um 1.1°C"]											//0x0B
		tempOffset << ["12"  : "Temperaturkorrektur um 1.2°C"]											//0x0C
		tempOffset << ["13"  : "Temperaturkorrektur um 1.3°C"]											//0x0D
		tempOffset << ["14"  : "Temperaturkorrektur um 1.4°C"]											//0x0E
		tempOffset << ["15"  : "Temperaturkorrektur um 1.5°C"]											//0x0F
		tempOffset << ["16"  : "Temperaturkorrektur um 1.6°C"]											//0x10
		tempOffset << ["17"  : "Temperaturkorrektur um 1.7°C"]											//0x11
		tempOffset << ["18"  : "Temperaturkorrektur um 1.8°C"]											//0x12
		tempOffset << ["19"  : "Temperaturkorrektur um 1.9°C"]											//0x13
		tempOffset << ["20"  : "Temperaturkorrektur um 2.0°C"]											//0x14
		tempOffset << ["21"  : "Temperaturkorrektur um 2.1°C"]											//0x15
		tempOffset << ["22"  : "Temperaturkorrektur um 2.2°C"]											//0x16
		tempOffset << ["23"  : "Temperaturkorrektur um 2.3°C"]											//0x17
		tempOffset << ["24"  : "Temperaturkorrektur um 2.4°C"]											//0x18
		tempOffset << ["25"  : "Temperaturkorrektur um 2.5°C"]											//0x19
		tempOffset << ["26"  : "Temperaturkorrektur um 2.6°C"]											//0x1A
		tempOffset << ["27"  : "Temperaturkorrektur um 2.7°C"]											//0x1B
		tempOffset << ["28"  : "Temperaturkorrektur um 2.8°C"]											//0x1C
		tempOffset << ["29"  : "Temperaturkorrektur um 2.9°C"]											//0x1D
		tempOffset << ["30"  : "Temperaturkorrektur um 3.0°C"]											//0x1E
		tempOffset << ["31"  : "Temperaturkorrektur um 3.1°C"]											//0x1F
		tempOffset << ["32"  : "Temperaturkorrektur um 3.2°C"]											//0x20
		tempOffset << ["33"  : "Temperaturkorrektur um 3.3°C"]											//0x21
		tempOffset << ["34"  : "Temperaturkorrektur um 3.4°C"]											//0x22
		tempOffset << ["35"  : "Temperaturkorrektur um 3.5°C"]											//0x23
		tempOffset << ["36"  : "Temperaturkorrektur um 3.6°C"]											//0x24
		tempOffset << ["37"  : "Temperaturkorrektur um 3.7°C"]											//0x25
		tempOffset << ["38"  : "Temperaturkorrektur um 3.8°C"]											//0x26
		tempOffset << ["39"  : "Temperaturkorrektur um 3.9°C"]											//0x27
		tempOffset << ["40"  : "Temperaturkorrektur um 4.0°C"]											//0x28
		tempOffset << ["41"  : "Temperaturkorrektur um 4.1°C"]											//0x29
		tempOffset << ["42"  : "Temperaturkorrektur um 4.2°C"]											//0x2A
		tempOffset << ["43"  : "Temperaturkorrektur um 4.3°C"]											//0x2B
		tempOffset << ["44"  : "Temperaturkorrektur um 4.4°C"]											//0x2C
		tempOffset << ["45"  : "Temperaturkorrektur um 4.5°C"]											//0x2D
		tempOffset << ["46"  : "Temperaturkorrektur um 4.6°C"]											//0x2E
		tempOffset << ["47"  : "Temperaturkorrektur um 4.7°C"]											//0x2F
		tempOffset << ["48"  : "Temperaturkorrektur um 4.8°C"]											//0x30
		tempOffset << ["49"  : "Temperaturkorrektur um 4.9°C"]											//0x31
		tempOffset << ["50"  : "Temperaturkorrektur um 5.0°C"]											//0x32
		tempOffset << ["128" : "Temperatur wird extern bereitgestellt"]									//0x80
    
	preferences {
		input "LCDinvert",        "enum", title: "Display invertieren?",               options: LCDinvertOptions,    description: "Default: Nein",                    required: false, displayDuringSetup: true
		input "LCDtimeout",       "enum", title: "Display ausschalten?",               options: LCDtimeoutOptions,   description: "Default: Immer an",                required: false, displayDuringSetup: true
		input "backlight",        "enum", title: "Hitergrundbeleuchtung",              options: backlightOptions,    description: "Default: Deaktiviert",             required: false, displayDuringSetup: true
		input "battNotification", "enum", title: "Batteryabfrage",                     options: batteryNotOptions,   description: "Default: 1 mal täglich",           required: false, displayDuringSetup: true 
		input "tempReport",       "enum", title: "Meldung bei Temperaturdifferenz",    options: tempReportRates,     description: "Default: Temperatur nicht melden", required: false, displayDuringSetup: true
		input "valveReport",      "enum", title: "Meldung bei Valvedifferenz",         options: valveReportRates,    description: "Default: Deaktiviert",             required: false, displayDuringSetup: true
		input "windowOpen",       "enum", title: "Fensteroffnungserkennung",           options: windowDetectOptions, description: "Default: Empfindlichkeit mittel",  required: false, displayDuringSetup: true
		input "tempOffset",       "enum", title: "Temperature offset",                 options: tempOffset,          description: "Default: Keine Korrektur",         required: false, displayDuringSetup: true
	}
}

def parse(String description) {
	def result = []
	if (description.startsWith("Err")) {
		result = createEvent(descriptionText: description, isStateChange: true)
	} else {
		def cmd = zwave.parse(description)
		if (cmd) { 
            		result += zwaveEvent(cmd) 
        	} else { 
            		log.debug "Non-parsed event: ${description}" 
        	}
    	}
	return result
}

def secure(hubitat.zwave.Command cmd) {
	if (state.sec) {
		return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
	} else { return cmd.format() }
}

def secureSequence(commands, delay=1500) {
    delayBetween(commands.collect{ secure(it) }, delay)
}

def initialize() {
	def cmd = []
	cmd << zwave.associationV2.associationGet()							//0x85:0x02
	cmd << zwave.associationV2.associationGroupingsGet()				//0x85:0x05
	cmd << zwave.associationV2.associationSpecificGroupGet()			//0x85:0x0B
 	cmd << zwave.associationGrpInfoV1.associationGroupNameGet()			//0x59:0x01 Short groupingIdentifier
	cmd << zwave.associationGrpInfoV1.associationGroupInfoGet()			//0x59:0x03 Short groupingIdentifier, Boolean listMode, Boolean refreshCache
	cmd << zwave.associationGrpInfoV1.associationGroupCommandListGet()	//0x59:0x05 Boolean allowCache, Short groupingIdentifier
	cmd << zwave.basicV1.basicGet()										//0x20:0x02
	cmd << zwave.batteryV1.batteryGet()									//0x80:0x02
	cmd << zwave.configurationV1.configurationGet(parameterNumber:1)	//0x70:0x05
	cmd << zwave.configurationV1.configurationGet(parameterNumber:2)	//0x70:0x05
	cmd << zwave.configurationV1.configurationGet(parameterNumber:3)	//0x70:0x05
	cmd << zwave.configurationV1.configurationGet(parameterNumber:4)	//0x70:0x05
	cmd << zwave.configurationV1.configurationGet(parameterNumber:5)	//0x70:0x05
	cmd << zwave.configurationV1.configurationGet(parameterNumber:6)	//0x70:0x05
	cmd << zwave.configurationV1.configurationGet(parameterNumber:7)	//0x70:0x05
	cmd << zwave.configurationV1.configurationGet(parameterNumber:8)	//0x70:0x05
	cmd << zwave.manufacturerSpecificV1.manufacturerSpecificGet()		//0x72:0x04
	cmd << zwave.notificationV8.eventSupportedGet(notificationType:8)	//0x71:0x01
	cmd << zwave.notificationV8.eventSupportedGet(notificationType:9)	//0x71:0x01
	cmd << zwave.protectionV1.protectionGet()							//0x75:0x02
	cmd << zwave.sensorMultiLevelV3.sensorMultilevelGet()				//0x31:0x04
	cmd << zwave.switchMultiLevelV1.switchMultilevelGet()				//0x26:0x02
	cmd << zwave.thermostatModeV2.thermostatModeGet()					//0x40:0x02
	cmd << zwave.thermostatModeV2.thermostatModeSupportedGet()			//0x40:0x04	Problematisch, Thermostat unterstützt nur V3-Version, hier keine Antwort
	cmd << zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType:11)	//0x43:0x02
	cmd << zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType:1)		//0x43:0x02
	cmd << zwave.securityV1.securityCommandsSupportedGet()				//0x98:0x02
	cmd << zwave.versionV2.versionGet()									//0x86:0x11
	cmd << zwave.zwaveplusInfoV2.zwaveplusInfoGet()						//0x5E:0x01
	secureSequence(cmd)
}

//AssociationV2
//Command Class: 0x85
//Command: 0x03
	def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationReport cmd) {
		//Short groupingIdentifier
		 //Short maxNodesSupported
		 //Short reportsToFollow 
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
	//Command: 0x06
	def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
	     //Short supportedGroupings 
	}
	//Command: 0x0C
	def zwaveEvent(hubitat.zwave.commands.associationv2.AssociationSpecificGroupReport cmd) {
	     //Short group
	}

//Association Grp Info V1
//Command Class: 0x59
	//Command: 0x02
	def zwaveEvent(hubitat.zwave.commands.associationgrpinfov1.AssociationGroupNameReport cmd) {
	     //Short groupingIdentifier
	     //Short lengthOfName
	     //List<AssociationGroupNameReport> name
	}
	//Command: 0x04
	def zwaveEvent(hubitat.zwave.commands.associationgrpinfov1.AssociationGroupInfoReport cmd) {
	     //Boolean dynamicInfo
	     //Short groupCount
	     //List<AssociationGroupInfoReport> groupInfo
	     //Boolean listMode
	}
	//Command: 0x06
	def zwaveEvent(hubitat.zwave.commands.associationgrpinfov1.AssociationGroupCommandListReport cmd) {
	     //List<AssociationGroupCommandListReport> command
	     //Short groupingIdentifier
	     //Short listLength
	}

def zwaveEvent(hubitat.zwave.commands.basicv1.BasicReport cmd) {
	def event = [ ]
	if (cmd.value == 0xFF) {
		state.thermostatMode = "heat" 				//es ist "heat"
	}
	if (cmd.value == 0xF0) {
		state.thermostatMode = "emergency heat" 	//es ist "full power"
	}
	if (cmd.value == 0x00) { 
		state.thermostatMode = "cool"				//es ist "energy save"
	}
	if (cmd.value == 0x0F) { 
		state.thermostatMode = "off"				//ist doch klar, oder?
	}
	if (cmd.value == 0xFE) { 
		state.thermostatMode = "manual"				//es ist "manufacturer specific"
	}
	event << createEvent(name: "thermostatMode", value: state.thermostatMode, displayed: true)
	log.info "Basic Report : ${cmd}, ${state.thermostatMode}"
	return event
}

def zwaveEvent(hubitat.zwave.commands.batteryv1.BatteryReport cmd) {
	state.lastBatteryReportReceivedAt = new Date().time 	// Store time of last battery
	createEvent(name: "battery", unit:"%", value: cmd.batteryLevel, isStateChange: true)
}

def zwaveEvent(hubitat.zwave.commands.configurationv1.ConfigurationReport cmd) {	
	switch (cmd.parameterNumber) {
		case 1: 
			def LCDinvertOptions = [:]
				LCDinvertOptions << ["0" : "Ja"]																
				LCDinvertOptions << ["1" : "Nein"]																
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "LCD Invertieren - ${LCDinvertOptions[msg]}"
			break;
		case 2: 
			def LCDtimeoutOptions = [:]
				LCDtimeoutOptions << ["0"  : "Immer an"]														//0x00
				LCDtimeoutOptions << ["1"  : "1 Secunde"]														//0x01
				LCDtimeoutOptions << ["2"  : "2 Secunden"]														//0x02
				LCDtimeoutOptions << ["3"  : "3 Secunden"]														//0x03
				LCDtimeoutOptions << ["4"  : "4 Secunden"]														//0x04
				LCDtimeoutOptions << ["6"  : "6 Secunden"]														//0x06
				LCDtimeoutOptions << ["7"  : "7 Secunden"]														//0x07
				LCDtimeoutOptions << ["8"  : "8 Secunden"]														//0x08
				LCDtimeoutOptions << ["9"  : "9 Secunden"]														//0x09
				LCDtimeoutOptions << ["10" : "10 Secunden"]														//0x0A
				LCDtimeoutOptions << ["11" : "11 Secunden"]														//0x0B
				LCDtimeoutOptions << ["12" : "12 Secunden"]														//0x0C
				LCDtimeoutOptions << ["13" : "13 Secunden"]														//0x0D
				LCDtimeoutOptions << ["14" : "14 Secunden"]														//0x0E
				LCDtimeoutOptions << ["15" : "15 Secunden"]														//0x0F
				LCDtimeoutOptions << ["16" : "16 Secunden"]														//0x10
				LCDtimeoutOptions << ["17" : "17 Secunden"]														//0x11
				LCDtimeoutOptions << ["18" : "18 Secunden"]														//0x12
				LCDtimeoutOptions << ["19" : "19 Secunden"]														//0x13
				LCDtimeoutOptions << ["20" : "20 Secunden"]														//0x14
				LCDtimeoutOptions << ["21" : "21 Secunden"]														//0x15
				LCDtimeoutOptions << ["22" : "22 Secunden"]														//0x16
				LCDtimeoutOptions << ["23" : "23 Secunden"]														//0x17
				LCDtimeoutOptions << ["24" : "24 Secunden"]														//0x18
				LCDtimeoutOptions << ["25" : "25 Secunden"]														//0x19
				LCDtimeoutOptions << ["26" : "26 Secunden"]														//0x1A
				LCDtimeoutOptions << ["27" : "27 Secunden"]														//0x1B
				LCDtimeoutOptions << ["28" : "28 Secunden"]														//0x1C
				LCDtimeoutOptions << ["29" : "29 Secunden"]														//0x1D
				LCDtimeoutOptions << ["30" : "20 Secunden"]														//0x1E
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "LCD Timeout - ${LCDtimeoutOptions[msg]}"
			break;
		case 3: 
			def backlightOptions = [:]
				backlightOptions << ["0" : "An"]																//0x00
				backlightOptions << ["1" : "Aus"]																//0x01	
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "Hintergrundbeleuchtung - ${backlightOptions[msg]}"
			break;
		case 4: 
			def batteryNotOptions = [:]
				batteryNotOptions << ["0" : "Eventgesteuert"]													//0x00
				batteryNotOptions << ["1" : "1 Mal täglich"]													//0x01
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "Batteriestatus senden - ${batteryNotOptions[msg]}"
			break;
		case 5: 
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
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "Aktuelle ${tempReportRates[msg]}"
			break;
		case 6:
			def valveReportRates = [:] 
				valveReportRates << ["0"  : "Deaktiviert"]														//0x00
				valveReportRates << ["1"  : "Ventilöffnungsgrad bei Delta von 1% melden"]						//0x01
				valveReportRates << ["2"  : "Ventilöffnungsgrad bei Delta von 2% melden"]						//0x02
				valveReportRates << ["3"  : "Ventilöffnungsgrad bei Delta von 3% melden"]						//0x03
				valveReportRates << ["4"  : "Ventilöffnungsgrad bei Delta von 4% melden"]						//0x04
				valveReportRates << ["5"  : "Ventilöffnungsgrad bei Delta von 5% melden"]						//0x05
				valveReportRates << ["6"  : "Ventilöffnungsgrad bei Delta von 6% melden"]						//0x06
				valveReportRates << ["7"  : "Ventilöffnungsgrad bei Delta von 7% melden"]						//0x07
				valveReportRates << ["8"  : "Ventilöffnungsgrad bei Delta von 8% melden"]						//0x08
				valveReportRates << ["9"  : "Ventilöffnungsgrad bei Delta von 9% melden"]						//0x09
				valveReportRates << ["10" : "Ventilöffnungsgrad bei Delta von 10% melden"]						//0x0A
				valveReportRates << ["11" : "Ventilöffnungsgrad bei Delta von 11% melden"]						//0x0B
				valveReportRates << ["12" : "Ventilöffnungsgrad bei Delta von 12% melden"]						//0x0C
				valveReportRates << ["13" : "Ventilöffnungsgrad bei Delta von 13% melden"]						//0x0D
				valveReportRates << ["14" : "Ventilöffnungsgrad bei Delta von 14% melden"]						//0x0E
				valveReportRates << ["15" : "Ventilöffnungsgrad bei Delta von 15% melden"]						//0x0F
				valveReportRates << ["16" : "Ventilöffnungsgrad bei Delta von 16% melden"]						//0x10
				valveReportRates << ["17" : "Ventilöffnungsgrad bei Delta von 17% melden"]						//0x11
				valveReportRates << ["18" : "Ventilöffnungsgrad bei Delta von 18% melden"]						//0x12
				valveReportRates << ["19" : "Ventilöffnungsgrad bei Delta von 19% melden"]						//0x13
				valveReportRates << ["20" : "Ventilöffnungsgrad bei Delta von 20% melden"]						//0x14
				valveReportRates << ["21" : "Ventilöffnungsgrad bei Delta von 21% melden"]						//0x15
				valveReportRates << ["22" : "Ventilöffnungsgrad bei Delta von 22% melden"]						//0x16
				valveReportRates << ["23" : "Ventilöffnungsgrad bei Delta von 23% melden"]						//0x17
				valveReportRates << ["24" : "Ventilöffnungsgrad bei Delta von 24% melden"]						//0x18
				valveReportRates << ["25" : "Ventilöffnungsgrad bei Delta von 25% melden"]						//0x19
				valveReportRates << ["26" : "Ventilöffnungsgrad bei Delta von 26% melden"]						//0x1A
				valveReportRates << ["27" : "Ventilöffnungsgrad bei Delta von 27% melden"]						//0x1B
				valveReportRates << ["28" : "Ventilöffnungsgrad bei Delta von 28% melden"]						//0x1C
				valveReportRates << ["29" : "Ventilöffnungsgrad bei Delta von 29% melden"]						//0x1D
				valveReportRates << ["30" : "Ventilöffnungsgrad bei Delta von 30% melden"]						//0x1E
				valveReportRates << ["31" : "Ventilöffnungsgrad bei Delta von 31% melden"]						//0x1F
				valveReportRates << ["32" : "Ventilöffnungsgrad bei Delta von 32% melden"]						//0x20
				valveReportRates << ["33" : "Ventilöffnungsgrad bei Delta von 33% melden"]						//0x21
				valveReportRates << ["34" : "Ventilöffnungsgrad bei Delta von 34% melden"]						//0x22
				valveReportRates << ["35" : "Ventilöffnungsgrad bei Delta von 35% melden"]						//0x23
				valveReportRates << ["36" : "Ventilöffnungsgrad bei Delta von 36% melden"]						//0x24
				valveReportRates << ["37" : "Ventilöffnungsgrad bei Delta von 37% melden"]						//0x25
				valveReportRates << ["38" : "Ventilöffnungsgrad bei Delta von 38% melden"]						//0x26
				valveReportRates << ["39" : "Ventilöffnungsgrad bei Delta von 39% melden"]						//0x27
				valveReportRates << ["40" : "Ventilöffnungsgrad bei Delta von 40% melden"]						//0x28
				valveReportRates << ["41" : "Ventilöffnungsgrad bei Delta von 41% melden"]						//0x29
				valveReportRates << ["42" : "Ventilöffnungsgrad bei Delta von 42% melden"]						//0x2A
				valveReportRates << ["43" : "Ventilöffnungsgrad bei Delta von 43% melden"]						//0x2B
				valveReportRates << ["44" : "Ventilöffnungsgrad bei Delta von 44% melden"]						//0x2C
				valveReportRates << ["45" : "Ventilöffnungsgrad bei Delta von 45% melden"]						//0x2D
				valveReportRates << ["46" : "Ventilöffnungsgrad bei Delta von 46% melden"]						//0x2E
				valveReportRates << ["47" : "Ventilöffnungsgrad bei Delta von 47% melden"]						//0x2F
				valveReportRates << ["48" : "Ventilöffnungsgrad bei Delta von 48% melden"]						//0x30
				valveReportRates << ["49" : "Ventilöffnungsgrad bei Delta von 49% melden"]						//0x31
				valveReportRates << ["50" : "Ventilöffnungsgrad bei Delta von 50% melden"]						//0x32	
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "Aktuelle ${valveReportRates[msg]}"
			break;
		case 7: 
			def windowDetectOptions = [:]
				windowDetectOptions << ["0" : "Deaktiviert"] 													//0x00
				windowDetectOptions << ["1" : "Empfindlichkeit niedrig"]										//0x01
				windowDetectOptions << ["2" : "Empfindlichkeit mittel"]											//0x02
				windowDetectOptions << ["3" : "Empfindlichkeit hoch"]											//0x03	
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "Fensteroffen Erkennung - ${windowDetectOptions[msg]}"
			break;
		case 8: 
			def tempOffset = [:]
				tempOffset << ["-50" : "Temperaturkorrektur um -5.0°C"]											//0xCE
				tempOffset << ["-49" : "Temperaturkorrektur um -4.9°C"]											//0xCF
				tempOffset << ["-48" : "Temperaturkorrektur um -4.8°C"]											//0xD0
				tempOffset << ["-47" : "Temperaturkorrektur um -4.7°C"]											//0xD1
				tempOffset << ["-46" : "Temperaturkorrektur um -4.6°C"]											//0xD2
				tempOffset << ["-45" : "Temperaturkorrektur um -4.5°C"]											//0xD3
				tempOffset << ["-44" : "Temperaturkorrektur um -4.4°C"]											//0xD4
				tempOffset << ["-43" : "Temperaturkorrektur um -4.3°C"]											//0xD5
				tempOffset << ["-42" : "Temperaturkorrektur um -4.2°C"]											//0xD6
				tempOffset << ["-41" : "Temperaturkorrektur um -4.1°C"]											//0xD7
				tempOffset << ["-40" : "Temperaturkorrektur um -4.0°C"]											//0xD8
				tempOffset << ["-39" : "Temperaturkorrektur um -3.9°C"]											//0xD9
				tempOffset << ["-38" : "Temperaturkorrektur um -3.8°C"]											//0xDA
				tempOffset << ["-37" : "Temperaturkorrektur um -3.7°C"]											//0xDB
				tempOffset << ["-36" : "Temperaturkorrektur um -3.6°C"]											//0xDC
				tempOffset << ["-35" : "Temperaturkorrektur um -3.5°C"]											//0xDD
				tempOffset << ["-34" : "Temperaturkorrektur um -3.4°C"]											//0xDE
				tempOffset << ["-33" : "Temperaturkorrektur um -3.3°C"]											//0xDF
				tempOffset << ["-32" : "Temperaturkorrektur um -3.2°C"]											//0xE0
				tempOffset << ["-31" : "Temperaturkorrektur um -3.1°C"]											//0xE1
				tempOffset << ["-30" : "Temperaturkorrektur um -3.0°C"]											//0xE2
				tempOffset << ["-29" : "Temperaturkorrektur um -2.9°C"]											//0xE3
				tempOffset << ["-28" : "Temperaturkorrektur um -2.8°C"]											//0xE4
				tempOffset << ["-27" : "Temperaturkorrektur um -2.7°C"]											//0xE5
				tempOffset << ["-26" : "Temperaturkorrektur um -2.6°C"]											//0xE6
				tempOffset << ["-25" : "Temperaturkorrektur um -2.5°C"]											//0xE7
				tempOffset << ["-24" : "Temperaturkorrektur um -2.4°C"]											//0xE8
				tempOffset << ["-23" : "Temperaturkorrektur um -2.3°C"]											//0xE9
				tempOffset << ["-22" : "Temperaturkorrektur um -2.2°C"]											//0xEA
				tempOffset << ["-21" : "Temperaturkorrektur um -2.1°C"]											//0xEB
				tempOffset << ["-20" : "Temperaturkorrektur um -2.0°C"]											//0xEC
				tempOffset << ["-19" : "Temperaturkorrektur um -1.9°C"]											//0xED
				tempOffset << ["-18" : "Temperaturkorrektur um -1.8°C"]											//0xEE
				tempOffset << ["-17" : "Temperaturkorrektur um -1.7°C"]											//0xEF
				tempOffset << ["-16" : "Temperaturkorrektur um -1.6°C"]											//0xF0
				tempOffset << ["-15" : "Temperaturkorrektur um -1.5°C"]											//0xF1
				tempOffset << ["-14" : "Temperaturkorrektur um -1.4°C"]											//0xF2
				tempOffset << ["-13" : "Temperaturkorrektur um -1.3°C"]											//0xF3
				tempOffset << ["-12" : "Temperaturkorrektur um -1.2°C"]											//0xF4
				tempOffset << ["-11" : "Temperaturkorrektur um -1.1°C"]											//0xF5
				tempOffset << ["-10" : "Temperaturkorrektur um -1.0°C"]											//0xF6
				tempOffset << ["-9"  : "Temperaturkorrektur um -0.9°C"]											//0xF7
				tempOffset << ["-8"  : "Temperaturkorrektur um -0.8°C"]											//0xF8
				tempOffset << ["-7"  : "Temperaturkorrektur um -0.7°C"]											//0xF9
				tempOffset << ["-6"  : "Temperaturkorrektur um -0.6°C"]											//0xFA
				tempOffset << ["-5"  : "Temperaturkorrektur um -0.5°C"]											//0xFB
				tempOffset << ["-4"  : "Temperaturkorrektur um -0.4°C"]											//0xFC
				tempOffset << ["-3"  : "Temperaturkorrektur um -0.3°C"]											//0xFD
				tempOffset << ["-2"  : "Temperaturkorrektur um -0.2°C"]											//0xFE
				tempOffset << ["-1"  : "Temperaturkorrektur um -0.1°C"]											//0xFF
				tempOffset << ["0"   : "Keine Korrektur"]              											//0x00
				tempOffset << ["1"   : "Temperaturkorrektur um 0.1°C"]											//0x01
				tempOffset << ["2"   : "Temperaturkorrektur um 0.2°C"]											//0x02
				tempOffset << ["3"   : "Temperaturkorrektur um 0.3°C"]											//0x03
				tempOffset << ["4"   : "Temperaturkorrektur um 0.4°C"]											//0x04
				tempOffset << ["5"   : "Temperaturkorrektur um 0.5°C"]											//0x05
				tempOffset << ["6"   : "Temperaturkorrektur um 0.6°C"]											//0x06
				tempOffset << ["7"   : "Temperaturkorrektur um 0.7°C"]											//0x07
				tempOffset << ["8"   : "Temperaturkorrektur um 0.8°C"]											//0x08
				tempOffset << ["9"   : "Temperaturkorrektur um 0.9°C"]											//0x09
				tempOffset << ["10"  : "Temperaturkorrektur um 1.0°C"]											//0x0A
				tempOffset << ["11"  : "Temperaturkorrektur um 1.1°C"]											//0x0B
				tempOffset << ["12"  : "Temperaturkorrektur um 1.2°C"]											//0x0C
				tempOffset << ["13"  : "Temperaturkorrektur um 1.3°C"]											//0x0D
				tempOffset << ["14"  : "Temperaturkorrektur um 1.4°C"]											//0x0E
				tempOffset << ["15"  : "Temperaturkorrektur um 1.5°C"]											//0x0F
				tempOffset << ["16"  : "Temperaturkorrektur um 1.6°C"]											//0x10
				tempOffset << ["17"  : "Temperaturkorrektur um 1.7°C"]											//0x11
				tempOffset << ["18"  : "Temperaturkorrektur um 1.8°C"]											//0x12
				tempOffset << ["19"  : "Temperaturkorrektur um 1.9°C"]											//0x13
				tempOffset << ["20"  : "Temperaturkorrektur um 2.0°C"]											//0x14
				tempOffset << ["21"  : "Temperaturkorrektur um 2.1°C"]											//0x15
				tempOffset << ["22"  : "Temperaturkorrektur um 2.2°C"]											//0x16
				tempOffset << ["23"  : "Temperaturkorrektur um 2.3°C"]											//0x17
				tempOffset << ["24"  : "Temperaturkorrektur um 2.4°C"]											//0x18
				tempOffset << ["25"  : "Temperaturkorrektur um 2.5°C"]											//0x19
				tempOffset << ["26"  : "Temperaturkorrektur um 2.6°C"]											//0x1A
				tempOffset << ["27"  : "Temperaturkorrektur um 2.7°C"]											//0x1B
				tempOffset << ["28"  : "Temperaturkorrektur um 2.8°C"]											//0x1C
				tempOffset << ["29"  : "Temperaturkorrektur um 2.9°C"]											//0x1D
				tempOffset << ["30"  : "Temperaturkorrektur um 3.0°C"]											//0x1E
				tempOffset << ["31"  : "Temperaturkorrektur um 3.1°C"]											//0x1F
				tempOffset << ["32"  : "Temperaturkorrektur um 3.2°C"]											//0x20
				tempOffset << ["33"  : "Temperaturkorrektur um 3.3°C"]											//0x21
				tempOffset << ["34"  : "Temperaturkorrektur um 3.4°C"]											//0x22
				tempOffset << ["35"  : "Temperaturkorrektur um 3.5°C"]											//0x23
				tempOffset << ["36"  : "Temperaturkorrektur um 3.6°C"]											//0x24
				tempOffset << ["37"  : "Temperaturkorrektur um 3.7°C"]											//0x25
				tempOffset << ["38"  : "Temperaturkorrektur um 3.8°C"]											//0x26
				tempOffset << ["39"  : "Temperaturkorrektur um 3.9°C"]											//0x27
				tempOffset << ["40"  : "Temperaturkorrektur um 4.0°C"]											//0x28
				tempOffset << ["41"  : "Temperaturkorrektur um 4.1°C"]											//0x29
				tempOffset << ["42"  : "Temperaturkorrektur um 4.2°C"]											//0x2A
				tempOffset << ["43"  : "Temperaturkorrektur um 4.3°C"]											//0x2B
				tempOffset << ["44"  : "Temperaturkorrektur um 4.4°C"]											//0x2C
				tempOffset << ["45"  : "Temperaturkorrektur um 4.5°C"]											//0x2D
				tempOffset << ["46"  : "Temperaturkorrektur um 4.6°C"]											//0x2E
				tempOffset << ["47"  : "Temperaturkorrektur um 4.7°C"]											//0x2F
				tempOffset << ["48"  : "Temperaturkorrektur um 4.8°C"]											//0x30
				tempOffset << ["49"  : "Temperaturkorrektur um 4.9°C"]											//0x31
				tempOffset << ["50"  : "Temperaturkorrektur um 5.0°C"]											//0x32
				tempOffset << ["128" : "Temperatur wird extern bereitgestellt"]									//0x80	
			def msg = cmd.scaledConfigurationValue.toString()
			log.info "Gemessene Temperatur hat ${tempOffset[msg]}"
			break;
	}
}

def zwaveEvent(hubitat.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
	createEvent (name: "tamper", value: "detected", displayed: true)
}

def zwaveEvent(hubitat.zwave.commands.manufacturerspecificv1.ManufacturerSpecificReport cmd) {
	if (cmd.manufacturerName) { updateDataValue("manufacturer", cmd.manufacturerName) }
	if (cmd.productTypeId) { updateDataValue("productTypeId", cmd.productTypeId.toString()) }
	if (cmd.productId) { updateDataValue("productId", cmd.productId.toString()) }
	if (cmd.manufacturerId){ updateDataValue("manufacturerId", cmd.manufacturerId.toString()) }
	log.info "ManufacturerSpecificReport : $cmd"
}

def zwaveEvent(hubitat.zwave.commands.notificationv8.NotificationReport cmd) {
	 //Short event
	 //Short eventParametersLength
	 //Short notificationStatus
	 //Short notificationType
	 //Short reserved
	 //Boolean sequence
	 //Short v1AlarmLevel
	 //Short v1AlarmType
	 //static Short NOTIFICATION_TYPE_POWER_MANAGEMENT = 8
	 //static Short NOTIFICATION_TYPE_SYSTEM = 9
	def events = []
	switch (cmd.notificationType) {
		case 8:
			def descriptionText
		if (cmd.eventParameter == []) {descriptionText = "neue Batterie"}
			if (cmd.eventParameter == [10]) { descriptionText = "25% Batterie verbleibend"}
			if (cmd.eventParameter == [11]) { descriptionText = "15% Batterie verbleibend" }
			sendEvent(name: "Notifity", value: descriptionText, displayed: false)			
			log.info "Power management event: Warning! $descriptionText"
			break;
		case 9:
			def descriptionText
			if (cmd.eventParameter == []) {
				descriptionText = "Cleared"
			} else {
				if (cmd.eventParameter == [1]) { descriptionText = "Kein Schließpunkt gefunden" }
				if (cmd.eventParameter == [2]) { descriptionText = "Keine Ventilbewegung möglich" }
				if (cmd.eventParameter == [3]) { descriptionText = "Kein Ventilschließpunkt gefunden" }
				if (cmd.eventParameter == [4]) { descriptionText = "Positionierung fehlgeschlagen"	}
			}
			sendEvent(name: "Notifity", value: descriptionText, displayed: false)
			log.info "System event: $descriptionText"
			break;
	}
	log.info "Notification Report : $cmd"
}


//funktioniert nicht
//gibt Antwort: ProtectionReport(localProtectionState:null, rfProtectionState:null)
//nicht documentierte localProtectionState! und rfProtectionState! was ist das?
def zwaveEvent(hubitat.zwave.commands.protectionv1.ProtectionReport cmd) { 
	log.info "ProtectionReport ${cmd}"
	def eventValue = ""
	if (cmd.localProtectionState == 0) {
		eventValue = "unlocked" 
		log.info "ProtectionReport: unlocked"
	}
	if (cmd.localProtectionState == 1) { 
		eventValue = "locked"
		log.info "ProtectionReport: locked"
	}
	if (eventValue != "") {
		createEvent(name: "lock", value: eventValue, displayed: true)
	}
}

def zwaveEvent(hubitat.zwave.commands.sensormultilevelv3.SensorMultilevelReport cmd) {
	def map = [ value: cmd.scaledSensorValue.toString(), displayed: true, name: "temperature", unit: "°C"]
	state.temperature = cmd.scaledSensorValue
	def gewuenscht = state.thermostatMode == "auto" ? state.heatingSetpoint : state.coolingSetpoint 
	if ((state.temperature < gewuenscht) && (state.thermostatMode != "manual") && (state.thermostatMode != "off") && (state.thermostatMode != "emergency heat" )) {
		sendEvent(name: "thermostatOperatingState", value: "heating", displayed: true)
	} else {
		sendEvent(name: "thermostatOperatingState", value: "idle", displayed: true)
	}
	log.info "Report Received : $cmd"
	createEvent(map)
}

def zwaveEvent(hubitat.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
	log.info "SwitchMultilevelReport Valve ist bei ${cmd.value}"
	createEvent(name:"level", value: cmd.value, unit:"%", displayed: true)
}
    
def zwaveEvent(hubitat.zwave.commands.thermostatmodev2.ThermostatModeReport cmd) {
	//Short mode
	//static Short MODE_ENERGY_SAVE_HEAT = 11
	//static Short MODE_HEAT = 1
	//static Short MODE_OFF = 0
	//static Short FULL_POWER = 0x0F
	//static Short MANUFACTURER_SPECIFIC = 0x1F
	// Hubitat supported ENUM ["heat", "cool", "emergency heat", "auto", "off"]
	def event = []
	if (cmd.mode == 0x01) { 
		state.thermostatMode = "heat" 
	}
	if (cmd.mode == 0x0F) { 
		state.thermostatMode = "emergency heat"          
	}
	if (cmd.mode == 0x11) { 
		state.thermostatMode = "cool" 
	}
	if (cmd.mode == 0) { 
		state.thermostatMode = "off"	
	}
	if (cmd.mode == 31) { 
		state.thermostatMode = "manual" 
		state.thermostatOperatingState = "vent economizer" 
	}
	event << createEvent(name: "thermostatMode", value: state.thermostatMode, displayed: true)
	log.info "ThermostatModeReport : ${cmd}, ${state.thermostatMode}"
	return event
}
//Command: 0x05
def zwaveEvent(hubitat.zwave.commands.thermostatmodev2.ThermostatModeSupportedReport cmd) {
	 //Boolean energySaveHeat
	 //Boolean heat
	 //Boolean off
	log.info "ThermostatModeSupportedReport ${cmd}"
	def supportedModes = []
	supportedModes << "off"
	supportedModes << "heat" 
	supportedModes << "cool" 
	supportedModes << "emergency heat" 
	supportedModes << "manual"
	supportedModes << "auto"
	state.supportedModes = supportedModes 
	sendEvent(name: "supportedThermostatModes", value: supportedModes, displayed: false)
	sendEvent(name: "supportedThermostatFanModes", value: ["auto"], displayed: false)
	sendEvent(name: "thermostatFanMode", value: "auto", displayed: false)
	log.info "Report Received : $cmd, Thermostat supported modes : $supportedModes"
}    
    
//Thermostat Setpoint V2
//Command Class: 0x43
	//Command: 0x03
	def zwaveEvent(hubitat.zwave.commands.thermostatsetpointv2.ThermostatSetpointReport cmd) {
	     //Short precision
	     //Short scale
	     //BigDecimal scaledValue
	     //Short setpointType
	     //Short size
	     //static Short SETPOINT_TYPE_ENERGY_SAVE_HEATING = 11
	     //static Short SETPOINT_TYPE_HEATING_1 = 1
		def event = []
		if (cmd.setpointType == 1) { 
			event << createEvent(name: "heatingSetpoint", value: cmd.scaledValue, unit: getTemperatureScale(), displayed: true)
			state.heatingSetpoint = cmd.scaledValue
		}
		if (cmd.setpointType == 11) { 
			event << createEvent(name: "coolingSetpoint", value: cmd.scaledValue, unit: getTemperatureScale(), displayed: true)
			state.coolingSetpoint = cmd.scaledValue
		}
		log.info "ThermostatSetpointReport ${cmd}"
		return event
	}
    
// V1
//Command Class: 0x98
	//Command: 0x03
	def zwaveEvent(hubitat.zwave.commands.securityv1.SecurityCommandsSupportedReport cmd) {
	     //List<SecurityCommandsSupportedReport> commandClassControl
	     //List<SecurityCommandsSupportedReport> commandClassSupport
	     //Short reportsToFollow
		log.info "SecurityCommandsSupportedReport ${cmd}"
	}

def zwaveEvent(hubitat.zwave.commands.securityv1.SecurityMessageEncapsulation cmd) {
	def encapsulatedCommand = cmd.encapsulatedCommand ([ 0x85:0x03, 0x20:0x03, 0x80:0x03, 0x70:0x06, 0x5A:0x01, 0x72:0x05, 0x71:0x05, 0x75:0x03, 0x31:0x05, 0x26:0x03, 0x40:0x03, 0x43:0x03, 0x86:0x12]) 
	if (encapsulatedCommand) {
		return zwaveEvent(encapsulatedCommand)
	} else {
	createEvent(descriptionText: cmd.toString())
	}
}

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
		def zWaveProtocolVersionDisp = String.format("%d.%02d",cmd.zWaveProtocolVersion,cmd.zWaveProtocolSubVersion)
		sendEvent([name: "zWaveLibraryType", value:  zWaveLibraryTypeDesc])
	}

def zwaveEvent(hubitat.zwave.commands.zwaveplusinfov2.ZwaveplusInfoReport cmd) {
	sendEvent([name: "zWaveplusNodeType", value: cmd.zWaveplusNodeType ])
	sendEvent([name: "zWaveplusRoleType", value: cmd.zWaveplusRoleType ])
	sendEvent([name: "zWaveplusVersion", value: cmd.zWaveplusVersion ])
}

def lock(){
 	def cmds = []
	cmds << zwave.protectionV1.protectionSet(protectionState: 1)
	cmds << zwave.protectionV1.protectionGet()
	sendEvent(name: "lock", value: "locked", displayed: true)
	secureSequence(cmds)
}

def unlock(){
	def cmds = []
	cmds << zwave.protectionV1.protectionSet(protectionState: 0)
	cmds << zwave.protectionV1.protectionGet()
	sendEvent(name: "lock", value: "unlocked", displayed: true)
	secureSequence (cmds)
}

def heat(){
	def cmds = []
    state.thermostatMode = "heat"
	cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x01)
	cmds << zwave.basicV1.basicSet(value: 0xFF)
	cmds << zwave.basicV1.basicGet()
	cmds << zwave.thermostatModeV2.thermostatModeGet()
	cmds << zwave.sensorMultilevelV3.sensorMultilevelGet()
	secureSequence (cmds)
}

def cool(){
	def cmds = []
    state.thermostatMode = "cool"
	cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x0B)
	cmds << zwave.basicV1.basicSet(value: 0x00)
	cmds << zwave.basicV1.basicGet()
	cmds << zwave.thermostatModeV2.thermostatModeGet()
	cmds << zwave.sensorMultilevelV3.sensorMultilevelGet()
	secureSequence(cmds)
}

def emergencyHeat(){
	def cmds = []
	state.thermostatMode = "emergency heat"
	sendEvent(name: "thermostatOperatingState", value: "heating", displayed: true)
    cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x0F)
	cmds << zwave.basicV1.basicSet(value: 0xF0)
	cmds << zwave.basicV1.basicGet()
	cmds << zwave.thermostatModeV2.thermostatModeGet()
	secureSequence(cmds)
}

def manual(){ //Manual-Mode
	def cmds = []
	state.thermostatMode = "manual"
	sendEvent(name: "thermostatMode", value: "manual", displayed: true)
    sendEvent(name: "thermostatOperatingState", value: "vent economizer", displayed: true)
    cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x1F)
	cmds << zwave.basicV1.basicSet(value: 0xFE)
	cmds << zwave.basicV1.basicGet()
	cmds << zwave.thermostatModeV2.thermostatModeGet()
	secureSequence(cmds)
}

def off(){
	def cmds = []
	state.thermostatMode = "off"
	sendEvent(name: "thermostatOperatingState", value: "idle", displayed: true)
    cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0)
	cmds << zwave.basicV1.basicSet(value: 0x0F)
	cmds << zwave.basicV1.basicGet()
	cmds << zwave.thermostatModeV2.thermostatModeGet()
	secureSequence (cmds)
}

def auto() {
    log.info "Das Modus Auto ist vom Gerät nicht unterstützt"
    def cmds = []
	state.thermostatMode = "manual"
	sendEvent(name: "thermostatMode", value: "manual", displayed: true)
    sendEvent(name: "thermostatOperatingState", value: "vent economizer", displayed: true)
    cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x1F)
	cmds << zwave.basicV1.basicSet(value: 0xFE)
	cmds << zwave.basicV1.basicGet()
	cmds << zwave.thermostatModeV2.thermostatModeGet()
	secureSequence(cmds)
} 

def setCoolingSetpoint(degrees){
	def cmds = []
	cmds << zwave.thermostatSetpointV2.thermostatSetpointSet(precision:1, scale:0, scaledValue: degrees, setpointType: 11)
	cmds << zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType: 11)
	cmds << zwave.sensorMultilevelV3.sensorMultilevelGet()
    secureSequence(cmds)
}

def setHeatingSetpoint(degrees){
	def cmds = []
	cmds << zwave.thermostatSetpointV2.thermostatSetpointSet(precision:1, scale:0, scaledValue: degrees, setpointType: 1)
	cmds << zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType: 1)
	cmds << zwave.sensorMultilevelV3.sensorMultilevelGet()
    secureSequence(cmds)
}

def setThermostatMode(String){
	def cmds = []    		
	switch (String) {
		case "manual":
			state.thermostatMode = "manual"
			sendEvent(name: "thermostatOperatingState", value: "vent economizer", displayed: true)
			cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x1F)
			cmds << zwave.basicV1.basicSet(value: 0xFE)
			cmds << zwave.basicV1.basicGet()
			cmds << zwave.thermostatModeV2.thermostatModeGet()
			break;
		case "emergency heat":
			state.thermostatMode = "emergency heat"
			sendEvent(name: "thermostatOperatingState", value: "heating", displayed: true)
			cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x0F)
			cmds << zwave.basicV1.basicSet(value: 0xF0)
			cmds << zwave.basicV1.basicGet()
			cmds << zwave.thermostatModeV2.thermostatModeGet()
			break;
		case "cool":
			state.thermostatMode = "cool"
			cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x0B)
			cmds << zwave.basicV1.basicSet(value: 0x00)
			cmds << zwave.basicV1.basicGet()
			cmds << zwave.thermostatModeV2.thermostatModeGet()
			cmds << zwave.sensorMultilevelV3.sensorMultilevelGet()
			break;
		case "heat":
        	state.thermostatMode = "heat"
			cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x01)
			cmds << zwave.basicV1.basicSet(value: 0xFF)
			cmds << zwave.basicV1.basicGet()
			cmds << zwave.thermostatModeV2.thermostatModeGet()
			cmds << zwave.sensorMultilevelV3.sensorMultilevelGet()
			break;
        case "off":
			state.thermostatMode = "off"
			sendEvent(name: "thermostatOperatingState", value: "idle", displayed: true)
			cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0)
			cmds << zwave.basicV1.basicSet(value: 0x0F)
			cmds << zwave.basicV1.basicGet()
			cmds << zwave.thermostatModeV2.thermostatModeGet()
			break;
        case "auto":
            state.thermostatMode = "manual"
			sendEvent(name: "thermostatOperatingState", value: "vent economizer", displayed: true)
			cmds << zwave.thermostatModeV2.thermostatModeSet(mode: 0x1F)
			cmds << zwave.basicV1.basicSet(value: 0xFE)
			cmds << zwave.basicV1.basicGet()
			cmds << zwave.thermostatModeV2.thermostatModeGet()
			break;
	}
	secureSequence (cmds)        	
}

def setThermostatFanMode(fanmode) {
	//Das Modell hat kein Fan
    log.info "Das Model besitzt kein Fan"
}

def setLevel(nextLevel) {
	def cmds = []
	if (state.thermostatMode == "manual") {
    		cmds << zwave.switchMultilevelV1.switchMultilevelSet(value: nextLevel)
	}
	cmds << zwave.switchMultilevelV1.switchMultilevelGet()
	secureSequence(cmds)
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
	}
}

def configure() {
	def LCDinvertL  		= stringToHexList (LCDinvert)
	def LCDtimeoutL 		= stringToHexList (LCDtimeout)
	def backlightL  		= stringToHexList (backlight)
	def battNotificationL 	= stringToHexList (battNotification)
	def tempReportL 		= stringToHexList (tempReport)
	def valveReportL 		= stringToHexList (valveReport)
	def windowOpenL 		= stringToHexList (windowOpen)
	def tempOffsetL 		= stringToHexList (tempOffset)
	def cmds = []
	cmds << zwave.configurationV1.configurationSet(configurationValue: LCDinvertL,		parameterNumber:1, size:1, scaledConfigurationValue: LCDinvertL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: LCDtimeoutL,		parameterNumber:2, size:1, scaledConfigurationValue: LCDtimeoutL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: backlightL,		parameterNumber:3, size:1, scaledConfigurationValue: backlightL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: battNotificationL, 	parameterNumber:4, size:1, scaledConfigurationValue: battNotificationL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: tempReportL,		parameterNumber:5, size:1, scaledConfigurationValue: tempReportL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: valveReportL,	parameterNumber:6, size:1, scaledConfigurationValue: valveReportL.get(0))
	cmds << zwave.configurationV1.configurationSet(configurationValue: windowOpenL,		parameterNumber:7, size:1, scaledConfigurationValue: windowOpenL.get(0))	
	cmds << zwave.configurationV1.configurationSet(configurationValue: tempOffsetL,		parameterNumber:8, size:1, scaledConfigurationValue: tempOffsetL.get(0))	
	cmds << zwave.sensorMultilevelV3.sensorMultilevelGet()     						//get temperature
	cmds << zwave.thermostatModeV2.thermostatModeGet()                              //get Mode
	cmds << zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType: 0x01)    //get temp heat
	cmds << zwave.thermostatSetpointV2.thermostatSetpointGet(setpointType: 0x0B)    //get temp eco
	cmds << zwave.switchMultilevelV1.switchMultilevelGet()                          //get valve position
	cmds << zwave.configurationV1.configurationGet(parameterNumber:1)               //get pamam - 1
	cmds << zwave.configurationV1.configurationGet(parameterNumber:2)               //get pamam - 2
	cmds << zwave.configurationV1.configurationGet(parameterNumber:3)               //get pamam - 3
	cmds << zwave.configurationV1.configurationGet(parameterNumber:4)               //get pamam - 4
	cmds << zwave.configurationV1.configurationGet(parameterNumber:5)               //get pamam - 5
	cmds << zwave.configurationV1.configurationGet(parameterNumber:6)               //get pamam - 6
	cmds << zwave.configurationV1.configurationGet(parameterNumber:7)               //get pamam - 7
	cmds << zwave.configurationV1.configurationGet(parameterNumber:8)               //get pamam - 8
	cmds << zwave.batteryV1.batteryGet()                                            //get battery stat
	cmds << zwave.protectionV1.protectionGet()                                      //secur
	cmds << zwave.thermostatModeV2.thermostatModeSupportedGet()                     //supportMode
	cmds << zwave.manufacturerSpecificV1.manufacturerSpecificGet()                  //fingerprint
	cmds << zwave.versionV2.versionGet()
	sendEvent(name: "configuration", value: "sent", displayed: true)
	sendEvent(name: "configure", value: "configure") 
	secureSequence(cmds)
}

def poll () {
    log.info "Polling ist nicht eingerichtet"
    //ich empfehle kein Polling, Gerät meldet selber, falls es nötig, die Änderungen
    //sonst machen die Einstellungen keinen Sinn
}
