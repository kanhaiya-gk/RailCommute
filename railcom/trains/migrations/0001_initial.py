# Generated by Django 3.2.6 on 2021-11-14 11:05

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Station',
            fields=[
                ('s_no', models.IntegerField(primary_key=True, serialize=False)),
                ('s_city', models.CharField(blank=True, max_length=50, null=True)),
            ],
            options={
                'db_table': 'station',
            },
        ),
        migrations.CreateModel(
            name='Train',
            fields=[
                ('train_no', models.IntegerField(primary_key=True, serialize=False)),
                ('t_name', models.CharField(max_length=20)),
                ('departure_time', models.CharField(max_length=10)),
                ('arrival_time', models.CharField(max_length=10)),
                ('dest_st', models.ForeignKey(db_column='dest_st', on_delete=django.db.models.deletion.DO_NOTHING, related_name='+', to='trains.station')),
                ('src_st', models.ForeignKey(db_column='src_st', on_delete=django.db.models.deletion.DO_NOTHING, related_name='+', to='trains.station')),
            ],
            options={
                'db_table': 'train',
            },
        ),
        migrations.CreateModel(
            name='StationTrains',
            fields=[
                ('s_no', models.OneToOneField(db_column='s_no', on_delete=django.db.models.deletion.DO_NOTHING, primary_key=True, serialize=False, to='trains.station')),
                ('arrival_time', models.CharField(max_length=10)),
                ('arrival_date', models.DateField()),
                ('hault', models.IntegerField()),
                ('fare', models.IntegerField()),
                ('train_no', models.ForeignKey(db_column='train_no', on_delete=django.db.models.deletion.DO_NOTHING, to='trains.train')),
            ],
            options={
                'db_table': 'station_trains',
                'unique_together': {('s_no', 'train_no', 'arrival_date')},
            },
        ),
    ]
