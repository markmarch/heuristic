# -*- coding: utf-8 -*-
#! /usr/bin/env python
import sys,math



def distance(a,b):
    return math.sqrt(sum([(x-y) ** 2 for  x,y in zip(a,b)]))



if len(sys.argv)==0:
    print "You need to provide as a first command line argument the file path for the cities"
    exit(1)

# loads the cities from the file
listofcities=['']
f = file(str(sys.argv[1]), 'r')
for line in f:
    listofcities.append(map(int,line.split()[1:]))
f.close()

# creates the vector that will check if you visited every city
listofcitiesvisited=[False]*len(listofcities)
listofcitiesvisited[0]=True;

# loads the path from stdin
path=map(int,list(input()));
if not (path[0]==1 and path[-1]==1):
    print "The path must end and start at city number 1"
    exit(1)

# calculates the length of the path
sumofdistances=0;
lastcity=path[0];
for city in path[1:]:
    sumofdistances+=distance(listofcities[city],listofcities[lastcity])
    listofcitiesvisited[city]=True
    lastcity=city

print "Your path is:"
print path
if listofcitiesvisited==[True]*len(listofcities):
    print "You visited every city and the total length of your path is:"
    print  sumofdistances
    exit(0)
else:
    print "You missed some cities on your way..."
    print "Cities missed: "+str([i for i,k in enumerate(listofcitiesvisited) if k==False])
    print "BUMP!"
    exit(1)
    
    