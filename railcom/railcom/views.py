from django.shortcuts import render
from rest_framework.decorators import api_view
from rest_framework import status
from rest_framework.response import Response
# Create your views here.

@api_view(['GET'])
def test(request):
    s = request.GET.get('st', None)
    q = request.GET.get('qt', None)
    d = {
        'qt' : q,
        'st' : s
    }
    return Response(d)