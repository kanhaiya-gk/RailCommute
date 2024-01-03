from django.db import models
from django.contrib.auth.models import AbstractUser

# Create your models here.
class Person(models.Model):
    first_name = models.CharField(max_length = 35)
    last_name = models.CharField(max_length = 35)

'''
class User(AbstractUser):
    email = models.EmailField(verbose_name='email', max_length=50)
    phone = models.CharField(null = False, unique = True)
    REQUIRED_FIELDS = ['username']


'''
