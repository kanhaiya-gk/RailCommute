from django.contrib import admin

# Register your models here.
from django.contrib import admin
from django.apps import apps
#from .models import *
# Register your models here.

#admin.site.register(Account)
app = apps.get_app_config('trains')

for model_name, model in app.models.items():
    admin.site.register(model)
