from rest_framework import serializers
from .models import Person

class PersonSerializer(serializers.ModelSerializer):
    class Meta:
        app_label = 'test1'
        model = Person
        fields = '__all__'