FROM openjdk:8
RUN mkdir /usr/src/myapp
WORKDIR /usr/src/myapp
LABEL author="lwtang@tmindtech.com"
VOLUME /tmp
EXPOSE 8080
COPY api.jar /usr/src/myapp/api.jar
ENTRYPOINT ["java","-jar","/usr/src/myapp/api.jar","--logging.path=/usr/src/myapp/log"]
