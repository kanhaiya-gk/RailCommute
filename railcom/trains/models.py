from django.db import models

# Create your models here.
class Station(models.Model):
    s_no = models.IntegerField(primary_key=True)
    s_city = models.CharField(max_length=50, blank=True, null=True)

    class Meta:
        db_table = 'station'

class Train(models.Model):
    train_no = models.IntegerField(primary_key=True)
    t_name = models.CharField(max_length=20)
    src_st = models.ForeignKey(Station, models.DO_NOTHING, db_column='src_st',related_name='+')
    departure_time = models.CharField(max_length=10)
    dest_st = models.ForeignKey(Station, models.DO_NOTHING, db_column='dest_st',related_name='+')
    arrival_time = models.CharField(max_length=10)

    class Meta:
        db_table = 'train'

class StationTrains(models.Model):
    s_no = models.OneToOneField(Station, models.DO_NOTHING, db_column='s_no', primary_key=True)
    train_no = models.ForeignKey(Train, models.DO_NOTHING, db_column='train_no')
    arrival_time = models.CharField(max_length=10)
    arrival_date = models.DateField()
    hault = models.IntegerField()
    fare = models.IntegerField()

    class Meta:
        db_table = 'station_trains'
        unique_together = (('s_no', 'train_no','arrival_date'),)


