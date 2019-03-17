#!/usr/bin/python
# -*- coding: UTF-8 -*-
import os

command = int(input("please input command:"))
if command == 0:
    exit(0)

if command == 1:
    childrenDirs = os.listdir('school_pics')

    for childrenDir in childrenDirs:
        childrenFiles = os.listdir('school_pics/' + childrenDir)
        for childrenFile in childrenFiles:
            os.rename('school_pics/' + childrenDir + '/' + childrenFile, 'school_pics/' + childrenFile)

elif command == 2:
    schoolData = open('schooldata.txt', 'r')
    lines = schoolData.readlines()
    for index in range(len(lines)):
        schoolName = lines[index].partition('###')[0]
        print("%d == %s" % (index, schoolName))
        newDir = "new_school_pics/%d" % (index + 1)
        os.makedirs(newDir)
        files = os.listdir('school_pics')
        for file in files:
            if file.count(schoolName) == 1:
                os.rename("school_pics/%s" % (file), "%s/%s" % (newDir, file))
    schoolData.close()

elif command == 3:
    childrenDirs = os.listdir('new_school_pics')
    schoolData = open('schooldata.txt', 'r')
    lines = schoolData.readlines()
    for childrenDir in childrenDirs:
        childrenFiles = os.listdir('new_school_pics/' + childrenDir)
        if len(childrenFiles) == 0:
            print(lines[int(childrenDir) - 1].partition('###')[0])

    schoolData.close()
