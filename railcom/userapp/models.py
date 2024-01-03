from django.db import models
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager
# Create your models here.

class MyUserManager(BaseUserManager):

    def create_user(self, first_name, last_name, email, mobile_no, password=None):
        if not first_name:
            raise ValueError("Must be valid")
        if not last_name:
            raise ValueError("Must be valid")
        if not email:
            raise ValueError("Must be valid")
        if not mobile_no:
            raise ValueError("Must be valid")
        
        user = self.model(
            first_name=first_name,
            last_name=last_name,
            email=self.normalize_email(email),
            mobile_no=mobile_no
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, first_name, last_name, email, mobile_no, password=None):
        user = self.create_user(
            first_name=first_name,
            last_name=last_name,
            email=email,
            mobile_no=mobile_no,
            password = password
        )
        user.is_admin = True
        user.is_staff = True
        user.is_superuser = True
        user.save(using=self._db)
        return user

class UserData(AbstractBaseUser):
    first_name = models.CharField(max_length=20)
    last_name = models.CharField(max_length=20, blank=True)
    email = models.CharField(max_length=50)
    mobile_no = models.CharField(max_length=10, unique=True)
    password = models.TextField(max_length=25, null=False)
    wallet = models.IntegerField(default=2000)
    date_joined = models.DateTimeField(auto_now_add=True)
    last_login = models.DateTimeField(auto_now=True)
    is_admin = models.BooleanField(default=False)
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    is_superuser = models.BooleanField(default=False)

    USERNAME_FIELD = 'mobile_no'
    REQUIRED_FIELDS = ['first_name', 'last_name', 'email']

    objects = MyUserManager()

    def __str__(self):
        return self.first_name

    def has_perm(self, perm, obj=None):
        return self.is_admin

    def has_module_perms(self, app_label):
        return True

