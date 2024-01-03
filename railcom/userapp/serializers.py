from django.db.models import fields
from djoser.serializers import UserCreateSerializer, UserSerializer
from rest_framework import serializers
from .models import *

class UserCreateSerializer(UserCreateSerializer):
    class Meta(UserCreateSerializer.Meta):
        model = UserData
        fields = ('id', 'first_name', 'last_name', 'email', 'password', 'mobile_no')