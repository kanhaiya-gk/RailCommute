from django.urls import path, include
from . import views

urlpatterns = [
    path('auth/', include('djoser.urls')),
    path('auth/', include('djoser.urls.authtoken')),
    path('usrtics/' , views.ticket_his),
    path('det/' , views.ticket_details),
    path('book/',views.book_tic),
    path('cancel/',views.cancel_tic),
    path('usr/' , views.find_uname),
    path('wallet/' , views.wallet),
]