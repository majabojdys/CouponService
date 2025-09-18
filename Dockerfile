FROM openjdk:21-jdk
WORKDIR /app
COPY target/CouponService-0.0.1-SNAPSHOT.jar couponservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "couponservice.jar"]