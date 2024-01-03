from django.urls import path, include
from . import views

urlpatterns = [
    #path('api/', views.cr, name = "cr"),
    #path('person-list/', views.PersonList, name = "person-list"),
    #path('person-detail/<str:pk>/', views.PersonDetail, name = "person-detail"),
    #path('add-person/', views.AddPerson, name = "add-person"),
    #path('update/<str:pk>/', views.Update, name = "update"),
    #path('delete/<str:pk>/', views.delete, name = "delete"),
    path('auth/', include('djoser.urls')),
    path('auth/', include('djoser.urls.authtoken')),
    #path('test123/', views.book_ticket, name = "add-person"),
    path('stat/' , views.trnbtstn),
    path('seats/' , views.num_seats),
    

]