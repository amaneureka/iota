#!/usr/bin/python
from gcm import *
import pymysql
import time

liveInstances = {}
gcm = GCM("AIzaSyCmbu04EeavKHwflPU1Xp-I3ymIKeWCFZM")

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
	sendNotification(hardwareID, "Hello")

while(1):
	db = pymysql.connect(host="localhost",
                    	 user="root",
                    	 passwd="root",
                    	 db="200OK")
	cursor = db.cursor()
	cursor.execute("SELECT * FROM logger")
	rows = cursor.fetchall()
	for row in rows:
		timeStamp = row[1]
		hardwareID = row[2]
		gpsLatitude = row[3]
		gpsLongitude = row[4]
		accX = row[5]
		accY = row[6]
		accZ = row[7]
		makePrediction(hardwareID, gpsLatitude, gpsLongitude, accX, accY, accZ)
		cursor.execute("DELETE FROM logger WHERE uid = " + str(row[0]))
		db.commit()
	cursor.close()
	db.close()
	time.sleep(0.5)
