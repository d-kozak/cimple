/**
 * Task executed by Heroku for building the app
 */
tasks.register("stage") {
    dependsOn("frontend:packageApp", "webserver:bootJar")
}