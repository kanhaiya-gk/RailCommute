from django.db import models
from userapp.models import UserData
from trains.models import Train

# Create your models here.
class Ticket(models.Model):
    ticket_id = models.AutoField(primary_key=True)
    train_no = models.ForeignKey(Train, models.DO_NOTHING, db_column='train_no')
    user_no = models.ForeignKey(UserData, models.DO_NOTHING)
    doboard = models.DateField(blank=True, null=True)
    doarrival = models.DateField(blank=True, null=True)
    transc_amount = models.IntegerField()
    trans_date = models.DateField()
    is_cancelled = models.BooleanField()

    class Meta:
        db_table = 'ticket'


class Passenger(models.Model):
    passenger_id = models.AutoField(primary_key=True)
    ticket = models.ForeignKey('Ticket', models.DO_NOTHING, blank=True, null=True)
    p_name = models.CharField(max_length=50)
    p_gender = models.CharField(max_length=6, blank=True, null=True)
    p_age = models.IntegerField()

    class Meta:
        db_table = 'passenger'


class Seat(models.Model):
    seat_no = models.CharField(primary_key=True, max_length=5)
    train_no = models.IntegerField()
    is_cancelled = models.BooleanField(default=True)
    passenger = models.ForeignKey(Passenger, models.DO_NOTHING, blank=True, null=True)

    class Meta:
        db_table = 'seat'
        unique_together = (('seat_no', 'train_no'),)




