#!/usr/bin/python
import gcm
import pymysql
import time
from collections import defaultdict

import dsp.thinkdsp

import numpy as np
from matplotlib import pyplot as plt

liveInstances = {}
gcm = gcm.GCM("AIzaSyCmbu04EeavKHwflPU1Xp-I3ymIKeWCFZM")

def sendNotification(hardwareID, message):
	db = pymysql.connect(host="localhost",
                     user="root",
                     passwd="root",
                     db="200OK")
	cursor = db.cursor()
	cursor.execute("SELECT * FROM link WHERE hid=" + str(hardwareID) + " LIMIT 1")
	num = int(cursor.rowcount)
	if (num == 0):
		print "Device not found!"
		return
	usr = cursor.fetchone()
	print "notify:: " + usr[1] + " -> " + message + " <> hid: " + str(hardwareID)
	gcm.plaintext_request(registration_id=usr[1], data={'message':message})
	cursor.close()
	db.close()

def makePrediction(hardwareID, gpsLatitude, gpsLongitude, accX, accY, accZ):
	liveInstances[hardwareID] = [gpsLatitude, gpsLongitude, accX, accY, accZ]

	# do all the algorithms
	sendNotification(hardwareID, "Manraj sir :p")
i=0
while(i<1):
	i+=1
	db = pymysql.connect(host="localhost",
                    	 user="root",
                    	 passwd="root",
                    	 db="200OK")
	cursor = db.cursor()
	cursor.execute("SELECT * FROM logger")
	rows = cursor.fetchall()
	GPSFreq_DICT = defaultdict(list)

	lastHardwareId = 0
	for row in rows:
		timeStamp = row[1]
		hardwareID = row[2]
		gpsLatitude = row[3]
		gpsLongitude = row[4]
		accX = row[5]
		accY = row[6]
		accZ = row[7]
		lastHardwareId = hardwareID
		#adding to the list of dictionary members
		GPSFreq_DICT[str(int(gpsLatitude))+'_'+str(int(gpsLongitude))].append(int(accZ))
		##makePrediction(hardwareID, gpsLatitude, gpsLongitude, accX, accY, accZ)
		##cursor.execute("DELETE FROM logger WHERE uid = " + str(row[0]))
		db.commit()
	cursor.close()
	db.close()

	#Frequency analysis
	for key in GPSFreq_DICT.keys():
		data = GPSFreq_DICT[key]
		length=len(data)
		print "Length:", length

		wave=dsp.thinkdsp.Wave(ys=data,framerate=4.1)
		spectrum=wave.make_spectrum()
		spectrum_analysis=wave.make_spectrum()

		fft_mag=list(np.absolute(spectrum.hs))
		fft_length= len(fft_mag)

		#spectrum_heart.high_pass(cutoff=0.5,factor=0)
		#spectrum_heart.low_pass(cutoff=4,factor=0)
		fft_heart=list(np.absolute(spectrum_analysis.hs))

		max_fft_heart=max(fft_heart)
		heart_sample=fft_heart.index(max_fft_heart)
		if max_fft_heart > 10:
			sendNotification(lastHardwareId, "Speed Breaker!")
		#hr=heart_sample*Fs/length*60
		#print "Heart Rate:", hr, "BPM"

		plt.subplot(211)
		wave.plot()
		##plt.xlim(20,60)
		plt.xlabel("Time(sec)")
		plt.title("Pulse Wave")
		plt.ylabel("Pulse Amplitude")
		plt.grid()
		plt.subplots_adjust(hspace=.5)
		plt.subplot(212)
		spectrum.plot()
		plt.ylim(0,max_fft_heart+500)
		plt.xlim(0,3)
		plt.title("Magnitude Spectrum")
		plt.xlabel("Frequency(Hz)")
		plt.ylabel("Magnitude")
		plt.grid()
		plt.show()

	time.sleep(2)