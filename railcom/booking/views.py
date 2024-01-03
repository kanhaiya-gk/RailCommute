from .models import Passenger, Seat , Ticket
from trains.models import Train , StationTrains , Station
from userapp.models import UserData
import json
from django.http import JsonResponse
from datetime import date
from rest_framework.decorators import permission_classes, api_view
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated


# Create your views here.
@api_view(['POST'])
@permission_classes([IsAuthenticated])
def ticket_his(request):
    mn=request.data.get('mobile_no')
    #u_id = UserDetails.objects.get(mobile_no=mn).user_id
    tic_lis = Ticket.objects.filter(user_no__mobile_no=mn)
    lis={}
    list=[]
    for tic in tic_lis:
        t_name = Train.objects.get(train_no=tic.train_no.train_no).t_name
        departure_time = Train.objects.get(train_no=tic.train_no.train_no).departure_time
        arrival_time = Train.objects.get(train_no=tic.train_no.train_no).arrival_time
        src_no = Train.objects.get(train_no=tic.train_no.train_no).src_st.s_no
        src_st = Station.objects.get(s_no=src_no).s_city
        dest_no = Train.objects.get(train_no=tic.train_no.train_no).dest_st.s_no
        dest_st = Station.objects.get(s_no=dest_no).s_city
        is_cancelled = tic.is_cancelled
        fare = tic.transc_amount
        dict={}
        dict['tic_id']=tic.ticket_id
        dict['train_no']=tic.train_no.train_no
        dict['t_name']=t_name
        dict['departure_time']=departure_time
        dict['arrival_time']=arrival_time
        dict['src_st']=src_st
        dict['dest_st']=dest_st
        dict['date_arr']=tic.doarrival
        dict['date_dep']=tic.doboard
        dict['cost']=fare
        dict['date_book']=tic.trans_date
        dict['is_cancelled']=is_cancelled
        list.append(dict)        
    lis['tics']=list
    return JsonResponse(lis)

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def ticket_details(request):
    d = json.dumps(request.data)
    data_p = json.loads(d)
    ticket = data_p['t_id']
    tic=Ticket.objects.get(ticket_id=ticket)
    trn_no=Ticket.objects.get(ticket_id=ticket).train_no.train_no
    t_name = Train.objects.get(train_no=trn_no).t_name
    #departure_time = Train.objects.get(train_no=tic.train_no.train_no).departure_time
    #arrival_time = Train.objects.get(train_no=tic.train_no.train_no).arrival_time
    src_no = Train.objects.get(train_no=trn_no).src_st.s_no
    #src_st = Station.objects.get(s_no=src_no).s_city
    dest_no = Train.objects.get(train_no=trn_no).dest_st.s_no
    #dest_st = Station.objects.get(s_no=dest_no).s_city
    fare = tic.transc_amount
    dict={}
    dict['tic_id']=ticket
    dict['train_no']=trn_no
    dict['t_name']=t_name
    #dict['departure_time']=departure_time
    #dict['arrival_time']=arrival_time
    #dict['src_st']=src_st
    #dict['dest_st']=dest_st
    #dict['date_arr']=tic.doarrival
    dict['date_dep']=tic.doboard
    dict['cost']=fare
    dict['is_cancelled']=tic.is_cancelled
    if tic.is_cancelled == False:
    #dict['date_book']=tic.trans_date
        p_list = []
        one_p={}
        pas = Passenger.objects.filter(ticket__ticket_id=ticket)
        for p in pas:
            one_p['p_name']=p.p_name
            one_p['p_gender']=p.p_gender
            one_p['p_age']=p.p_age
            one_p['p_seat']=Seat.objects.get(passenger=p).seat_no
            p_list.append(one_p)
        dict['p_list']=p_list
    return JsonResponse(dict, safe=False)

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def book_tic(request):
    d = json.dumps(request.data)
    data_p = json.loads(d)
    trans_date = date.today()
    dest_no=Station.objects.get(s_city=data_p['st2']).s_no
    src_no=Station.objects.get(s_city=data_p['st1']).s_no
    tr_no = Train.objects.get(train_no=data_p['train_no'])
    u_id=UserData.objects.get(mobile_no=data_p['mn'])
    p_list=list(data_p['p_list'])
    num_pass = len(p_list)
    transc_amount = (StationTrains.objects.filter(train_no=data_p['train_no']).get(s_no=dest_no).fare) - (StationTrains.objects.filter(train_no=data_p['train_no']).get(s_no=src_no).fare)
    transc_amount *= num_pass
    tic = Ticket(train_no=tr_no,user_no=u_id,doboard=data_p['date_book'],doarrival=data_p['date_book'],transc_amount=(transc_amount),trans_date=trans_date,is_cancelled=False)
    tic.save()
    tid=tic.ticket_id
    t = Ticket.objects.get(ticket_id=tid)
    for one_pass in p_list:
        one_p = dict(one_pass)
        all_seat = Seat.objects.filter(train_no = tr_no.train_no,is_cancelled=True).first()
        
        p=Passenger(ticket=t,p_name=one_p['p_name'],p_age=one_p['p_age'],p_gender=one_p['p_gender'])
        p.save()
        s = Seat.objects.get(seat_no=all_seat.seat_no,train_no=all_seat.train_no)
        s.is_cancelled = False
        s.passenger = p
        s.save()
    u = UserData.objects.get(mobile_no=tic.user_no.mobile_no)
    u.wallet = u.wallet - transc_amount
    u.save()
    msg={'t_id':tid}
    return JsonResponse(msg)
    

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def cancel_tic(request):
    d = json.dumps(request.data)
    data_p = json.loads(d)
    t_id = data_p['t_id']
    try:
        t = Ticket.objects.get(ticket_id=t_id)
    except Ticket.DoesNotExist:
        return Response({'Status':'false'})
    if t.is_cancelled == True:
        return Response({'Status':'false'})
    
    t.is_cancelled=True
    t.save()
    p_l = Passenger.objects.filter(ticket__ticket_id=t_id)
    for p in p_l:
        s = Seat.objects.get(passenger__passenger_id=p.passenger_id)
        s.is_cancelled = True
        s.passenger = None
        s.save()
    fare = t.transc_amount
    u = UserData.objects.get(mobile_no=t.user_no.mobile_no)
    u.wallet = fare + u.wallet
    u.save()
    return Response({'Status':'true'})
    
@api_view(['GET'])
@permission_classes([IsAuthenticated])
def find_uname(request):
    no=request.GET.get('mn')
    u_name = str(UserData.objects.get(mobile_no=no).first_name) + " " + str(UserData.objects.get(mobile_no=no).last_name)
    msg = {'u_name':u_name}
    return JsonResponse(msg)

@api_view(['GET'])
@permission_classes([IsAuthenticated])
def wallet(request):
    no=request.GET.get('mn')
    amount = UserData.objects.get(mobile_no=no).wallet
    msg = {'amount':amount}
    return JsonResponse(msg)



