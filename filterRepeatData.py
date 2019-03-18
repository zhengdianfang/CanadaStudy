#!/usr/bin/python
# -*- coding: UTF-8 -*- 
file = open('schooldata.txt', 'r+')
file1 = open('schooldata1.txt', 'a')
lineList = file.readlines()
count = 0
nameList = []
repeatNames = []
allLineMap = {} 
for line in lineList:
    name = line.split('###')[1]
    print(name)
    allLineMap[name] = line
    count += 1
print(count)
print(len(allLineMap))
file1.writelines(allLineMap.values())
file.close()
file1.close()
