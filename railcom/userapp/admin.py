from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import UserData
# Register your models here.

class useradmin(UserAdmin):
    ordering = ('mobile_no', 'first_name', 'last_name', 'email', 'date_joined', 'last_login', 'is_admin', 'is_staff')
    list_display = ['mobile_no', 'first_name', 'last_name', 'email', 'date_joined', 'last_login', 'is_admin', 'is_staff']
    search_fields = ('mobile_no', 'email')
    readonly_fields = ('id', 'date_joined', 'last_login')
    add_fieldsets = (
        (None, {'fields': ('mobile_no', 'first_name', 'last_name', 'email', 'date_joined', 'last_login', 'is_admin', 'is_staff')}),
    )

    filter_horizontal = ()
    list_filter = ()
    fieldsets = ()

admin.site.register(UserData, useradmin)