from django.shortcuts import render
from django.http import JsonResponse

from rest_framework.decorators import api_view,permission_classes
from rest_framework.permissions import IsAuthenticated
import json
from rest_framework.response import Response

from .serializers import PersonSerializer
from .models import Person

# Create your views here.

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def book_ticket(request):
    #j_data = request.data.get('mn')
    d = json.dumps(request.data)
    data_p = json.loads(d)
    msg={'Response':data_p['mn']}
    return Response(msg,content_type="application/json")

@api_view(['GET'])
def cr(request):
    api_urls = {
        'List' : '/person-list/',
        'Detail View' : '/task-details/<str:pk>/'
    }
    return Response(api_urls)

@api_view(['GET'])
def PersonList(request):
    pers = Person.objects.all()
    serializer = PersonSerializer(pers, many = True)
    return Response(serializer.data)

@api_view(['GET'])
def PersonDetail(request, pk):
    pers = Person.objects.get(id = pk)
    serializer = PersonSerializer(pers, many = False)
    return Response(serializer.data)

@api_view(['POST'])
def AddPerson(request):
    serializer = PersonSerializer(data = request.data)

    if serializer.is_valid():
        serializer.save()

    return Response(serializer.data)

@api_view(['POST'])
def Update(request, pk):
    pers = Person.objects.get(id = pk)
    serializer = PersonSerializer(instance = pers, data = request.data)

    if serializer.is_valid():
        serializer.save()

    return Response(serializer.data)

@api_view(['DELETE'])
def delete(request, pk):
    pers = Person.objects.get(id = pk)
    pers.delete()

    return Response({'id' : pk, 'Operation' : 'Deleted'})