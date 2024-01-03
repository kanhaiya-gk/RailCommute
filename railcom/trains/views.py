from django.db.models.fields import NullBooleanField
from django.db import connection
from django.shortcuts import render
from rest_framework.renderers import JSONRenderer
from userapp.models import UserData
#from .serializers import TrainSerializer,StationSerializer,StationTrainsSerializer
from .models import StationTrains,Station,Train
from booking.models import Passenger, Seat
from django.http import HttpResponse, JsonResponse
import json
import datetime
import time

def parse_date(s):
    date = s.rsplit(":")
    return time.mktime(time.strptime(date,"%H:%M"))


# Create your views here.
def trnbtstn(request):
    if request.method=='GET':
        stn1 = request.GET.get('st1')
        s1_no = Station.objects.get(s_city=stn1)
        stn2 = request.GET.get('st2')
        s2_no = Station.objects.get(s_city=stn2)
        date = request.GET.get('date_search')
        trn_lis1 = StationTrains.objects.filter(s_no=s1_no)
        trn_lis2 = StationTrains.objects.filter(s_no=s2_no)
        trn_lis=[]
        for t1,t2 in zip(trn_lis1,trn_lis2):
            if t1.train_no.train_no == t2.train_no.train_no:
                if t1.arrival_date == t2.arrival_date:
                    #time1=(t1.arrival_time)
                    #time2=(t2.arrival_time)
                    #h1=time1.split(':',1)[0]
                    #h2=time2.split(':',1)[0]
                    #m1=time1.split(':',1)[1]
                    #m2=time2.split(':',1)[1]
                    #if h1 > h2:
                    #    trn_lis.append(t1.train_no)
                    #elif h1==h2 and m1>m2:
                        trn_lis.append(t1.train_no)
        lis=[]
        lisd={}
        for trn in trn_lis:
            t_name = trn.t_name
            departure_time = trn.departure_time
            arrival_time = trn.arrival_time
            src_no = s1_no.s_no
            src_st = Station.objects.get(s_no=src_no).s_city
            dest_no = s2_no.s_no
            dest_st = Station.objects.get(s_no=dest_no).s_city
            fare = (StationTrains.objects.filter(train_no=trn.train_no).get(s_no=dest_no).fare) - (StationTrains.objects.filter(train_no=trn.train_no).get(s_no=src_no).fare)
            dict={}
            dict['train_no']=trn.train_no
            dict['t_name']=t_name
            dict['departure_time']=departure_time
            dict['arrival_time']=arrival_time
            dict['src_st']=src_st
            dict['dest_st']=dest_st
            dict['date_arr']=date
            dict['date_dep']=date
            dict['cost']=fare
            lis.append(dict)
        lisd['trains']=lis
        return JsonResponse(lisd)
        

def num_seats(request):
    if request.method=='GET':
        stn1 = request.GET.get('st1')
        stn2 = request.GET.get('st2')
        trn = request.GET.get('tr')
        
        trn_nos = Train.objects.get(train_no=trn)
        trn_no = trn_nos.train_no
        all_seat = Seat.objects.filter(train_no = trn_no)
        all_avail_seats = Seat.objects.filter(is_cancelled=True)
        avail_seats = (all_seat&all_avail_seats).values('seat_no','train_no')
        num = avail_seats.count()
        
        num_dict = {'num' : num}
        json_data = json.dumps(num_dict)
        return HttpResponse(json_data, content_type ='application/json')
    num_dict = {'num' : 0}
    json_data = json.dumps(num_dict)
    return HttpResponse(json_data, content_type ='application/json')



