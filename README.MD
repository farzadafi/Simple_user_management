<h2>Simple User Management</h2>

<p>hi, first please pull <b>haravich/fake-smtp-server
</b> with command:</p>

```
docker pull haravich/fake-smtp-server
```

<p>after successfully pull image, run it with command:</p>

```
docker run -d -p 1025:1025 -p 1080:1080 haravich/fake-smtp-server
```

<p>after that please clone this repository and start app and go to swagger page:</p>
<a href="http://localhost:8080/swagger-ui/index.html">swagger_page</a>

<b>note:</b>
<p>just for first run of this app set <b>always</b> for <b>spring.sql.init.mode</b> and after set on never :) </p>

<h3>Thanks for your time and energy</h3>