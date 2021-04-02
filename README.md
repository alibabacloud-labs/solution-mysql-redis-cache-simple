### Project URL
[https://github.com/alibabacloud-labs/solution-mysql-redis-cache-simple](https://github.com/alibabacloud-labs/solution-mysql-redis-cache-simple)


### Architecture Overview
![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1616740109452-4c94ae78-a656-4cfa-86eb-daf7bb72531b.png#height=371&id=au2CM&margin=%5Bobject%20Object%5D&name=image.png&originHeight=741&originWidth=856&originalType=binary&size=76676&status=done&style=none&width=428)


### Deployment
#### Terraform
Use terraform to provision ECS, Redis and RDS MySQL instances that used in this solution against this .tf file:
[https://github.com/alibabacloud-labs/solution-mysql-redis-cache-simple/blob/main/deployment/terraform/main.tf](https://github.com/alibabacloud-labs/solution-mysql-redis-cache-simple/blob/main/deployment/terraform/main.tf)


For more information about how to use Terraform, please refer to this tutorial: [https://www.youtube.com/watch?v=zDDFQ9C9XP8](https://www.youtube.com/watch?v=zDDFQ9C9XP8)
For the video tutorial about how to use Redis and RDS MySQL, and cache retiring based on TTL in Redis, please refer to: [https://www.youtube.com/watch?v=_JqcsKyDljw](https://www.youtube.com/watch?v=_JqcsKyDljw)


### Run Demo
#### Step 1: Log on ECS & setup environment
In this tutorial, Alibaba Cloud Linux is used,  when you check with the terraform script, you will see the image id is: aliyun_2_1903_x64_20G_alibase_20200904.vhd
![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1614589525507-61c19a9c-1456-4d77-8c29-8cc635ccf693.png#height=616&id=oECNI&margin=%5Bobject%20Object%5D&name=image.png&originHeight=616&originWidth=1288&originalType=binary&size=127500&status=done&style=none&width=1288)
Reset the ECS password here and restart the instance to apply the change.


![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1614589615475-34d74563-beb2-47f4-8731-2493292c8f0a.png#height=369&id=8okl1&margin=%5Bobject%20Object%5D&name=image.png&originHeight=369&originWidth=957&originalType=binary&size=49149&status=done&style=none&width=957)
![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1614589655087-42f42ede-9529-46e6-a22c-a98bc6594219.png#height=417&id=5UOQB&margin=%5Bobject%20Object%5D&name=image.png&originHeight=417&originWidth=645&originalType=binary&size=41619&status=done&style=none&width=645)
Click “Connect” button to log on ECS via VNC.
![image.png](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1614589704900-57f20754-057a-490a-8216-93a67b568e0d.png#height=309&id=UbJlL&margin=%5Bobject%20Object%5D&name=image.png&originHeight=309&originWidth=599&originalType=binary&size=21690&status=done&style=none&width=599)
You need to modify VNC password for the 1st time for logging on the ECS.
![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617329873981-ed254ea1-3ed6-46a2-a00e-ae3719d64d22.png#from=paste&height=239&id=u0ccfa290&margin=%5Bobject%20Object%5D&originHeight=239&originWidth=1061&originalType=binary&size=26748&status=done&style=none&taskId=ucebac197-b797-43ce-9e3d-2930b31fd5a&width=1061)
Click "Enter Copy Commands" to execute the following commands one by one, which are to keep the apt update, install Java, Maven and MySQL client for building and running the demo application later.
![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617329907991-ae1a4222-a15c-47a1-af9d-76e55841e032.png#from=paste&height=486&id=u180a8a3b&margin=%5Bobject%20Object%5D&originHeight=486&originWidth=854&originalType=binary&size=48949&status=done&style=none&taskId=ud7c4c7d2-e70a-4135-be6e-ff450258edf&width=854)
```bash
yum install java-latest-openjdk.x86_64
yum install maven
yum install mysql
```






#### Step 2: Modify & run application
Click "Enter Copy Commands" to execute the following commands one by one, which are to download the demo application code, then build the demo Java application.
```bash
wget https://github.com/alibabacloud-labs/solution-mysql-redis-cache-simple/raw/main/source.tar.gz
tar xvf source.tar.gz && cd source
mvn clean package assembly:single -DskipTests
```
Edit the env.sh file to set the Redis and RDS MySQL endpoints (you can get from Redis and RDS MySQL web console) accordingly.

- Redis endpoint:

![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617331047330-dfde659a-3f58-43f6-8cdf-f29f6485e908.png#from=paste&height=703&id=ubffd6cb2&margin=%5Bobject%20Object%5D&originHeight=703&originWidth=1103&originalType=binary&size=213690&status=done&style=none&taskId=ufd120df3-c89f-4bfa-af8d-310645d8d59&width=1103)

- RDS MySQL endpoint:

![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617331038156-79d2d9d1-4786-4815-aed9-ff78c84634d0.png#from=paste&height=390&id=u2ad66580&margin=%5Bobject%20Object%5D&originHeight=390&originWidth=1315&originalType=binary&size=194871&status=done&style=none&taskId=u1a40d30a-f260-4b77-a323-b5f1a83d835&width=1315)

- Edit to apply the Redis and RDS MySQL endpoints

![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617333006833-2b49abf8-b2e3-42af-95c3-4f5ca05ae1e6.png#from=paste&height=206&id=uf497b4cd&margin=%5Bobject%20Object%5D&originHeight=206&originWidth=572&originalType=binary&size=61746&status=done&style=none&taskId=ufe0b3e7a-e065-4a3f-b77a-cf30a7254e4&width=572)
![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617332992471-6dc89599-b7a8-4d91-8c5a-1ebf49c93e6e.png#from=paste&height=252&id=u950583d5&margin=%5Bobject%20Object%5D&originHeight=252&originWidth=906&originalType=binary&size=129887&status=done&style=none&taskId=u32392651-64bc-4dc2-ba11-c01bf954fa6&width=906)
Use the mysql_client.sh to create a demo table and insert some records into the RDS MySQL.
```bash
sh mysql_client.sh < rosebowl.sql
```
![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617333122975-c1d506b3-404e-4399-870c-cf93820cc304.png#from=paste&height=354&id=uf1b459bb&margin=%5Bobject%20Object%5D&originHeight=354&originWidth=668&originalType=binary&size=108156&status=done&style=none&taskId=uca591fee-8424-4fa7-8a4f-ed28186bfbf&width=668)
You can also use mysql_client.sh to query the data in RDS MySQL. There should be 30 records now.
![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617334531403-96e6bfa0-1716-4bbc-b614-19de6c132666.png#from=paste&height=45&id=u1c6a54d4&margin=%5Bobject%20Object%5D&originHeight=45&originWidth=530&originalType=binary&size=5378&status=done&style=none&taskId=u79b85fb4-3725-4142-a5db-9c37a485bde&width=530)
![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617334613037-7752b887-2afa-4f99-873b-c6c30fd223ba.png#from=paste&height=646&id=u24c2c870&margin=%5Bobject%20Object%5D&originHeight=646&originWidth=499&originalType=binary&size=117271&status=done&style=none&taskId=u2b5c6c04-b83e-4330-a0fd-f7117ab2b3d&width=499)
Run the Redis demo application to verify the cache and TTL logic. You can pick up a query from the file query.sql to run. The default TTL is set as 10 seconds in the demo application. It will tell you that the result is fetched from MySQL or Redis cache. Do remember that the life time of query result in Redis cache is only 10 seconds. If you want to make it longer, you can modify the code [https://github.com/alibabacloud-labs/solution-mysql-redis-cache-simple/blob/main/source/src/main/java/demo/RedisCache.java](https://github.com/alibabacloud-labs/solution-mysql-redis-cache-simple/blob/main/source/src/main/java/demo/RedisCache.java) and build to run again.
```bash
sh run_sample_app.sh
```
![](https://intranetproxy.alipay.com/skylark/lark/0/2021/png/32590/1617336193105-06965e38-0f2e-4047-92e4-8caae6814768.png#from=paste&height=491&id=u6a561030&margin=%5Bobject%20Object%5D&originHeight=491&originWidth=1124&originalType=binary&size=174152&status=done&style=none&taskId=u0921142b-fa1b-4277-9db2-b49d4ebaa2b&width=1124)
