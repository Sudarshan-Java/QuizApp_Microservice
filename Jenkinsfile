pipeline {

    agent any

    options {
        retry(conditions: [nonresumable()], count: 2)
        durabilityHint('PERFORMANCE_OPTIMIZED')
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    environment {

        // ============================================================
        // JAVA / MAVEN
        // ============================================================

        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-17.0.2'
        MAVEN_HOME = 'D:\\Softwarepath\\apache-maven-3.8.5'


        // ============================================================
        // PROJECT
        // ============================================================

        PROJECT_DIR = '.'

        // CHANGE THIS
        APP_JAR = 'target\\quiz-app-backend-0.0.1-SNAPSHOT.jar'


        // ============================================================
        // BACKEND
        // ============================================================

        // CHANGE THIS IF YOUR OTHER PROJECT USES ANOTHER PORT
        BACKEND_PORT = '8001'

        BACKEND_URL = 'http://localhost:8001'


        // ============================================================
        // FRONTEND WAR
        // ============================================================

        // CHANGE THESE PATHS FOR YOUR OTHER PROJECT

        APPZILLON_SERVER_WAR =
            'C:\\deploy\\Quiz_App\\AppzillonServer.war'

        FRONTEND_WAR =
            'C:\\deploy\\Quiz_App\\QuizAp.war'


        // ============================================================
        // TOMCAT CONTEXT
        // ============================================================

        // CHANGE THESE

        APPZILLON_CONTEXT = 'AppzillonServer'
        FRONTEND_CONTEXT = 'QuizAp'


        // ============================================================
        // TOMCAT
        // ============================================================

        // Usually this remains the same if Jenkins is running
        // on the same Windows machine.

        TOMCAT_HOME = 'D:\\Softwarepath\\apache-tomcat-9.0.53'
        TOMCAT_PORT = '8080'


        // ============================================================
        // FRONTEND
        // ============================================================

        // CHANGE THIS

        FRONTEND_URL =
            'http://localhost:8080/QuizAp/'


        // ============================================================
        // PLAYWRIGHT
        // ============================================================

        // CHANGE THIS TO THE LOCATION OF YOUR AUTOMATION PROJECT

        PLAYWRIGHT_DIR =
            'C:\\Users\\sudarshan.khot\\Downloads\\quiz-app-backend'

    }


    stages {


        // ============================================================
        // 1. CHECKOUT
        // ============================================================

        stage('Checkout') {

            steps {

                echo '=========================================='
                echo 'CHECKOUT PROJECT'
                echo '=========================================='

                checkout scm

                bat '''
                    @echo off

                    echo Current workspace:
                    cd

                    echo.
                    echo Workspace contents:
                    dir

                    echo.
                    echo Git status:
                    git status
                '''
            }
        }


        // ============================================================
        // 2. VERIFY ENVIRONMENT
        // ============================================================

        stage('Verify Environment') {

            steps {

                echo '=========================================='
                echo 'VERIFYING ENVIRONMENT'
                echo '=========================================='

                bat '''
                    @echo off

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%MAVEN_HOME%\\bin;%PATH%"

                    echo.
                    echo JAVA_HOME:
                    echo %JAVA_HOME%

                    echo.
                    echo MAVEN_HOME:
                    echo %MAVEN_HOME%

                    echo.
                    echo JAVA VERSION:
                    java -version

                    echo.
                    echo MAVEN VERSION:
                    mvn -version

                    echo.
                    echo Checking project...

                    if not exist "%WORKSPACE%\\%PROJECT_DIR%\\pom.xml" (

                        echo.
                        echo ==========================================
                        echo ERROR: pom.xml NOT FOUND
                        echo ==========================================

                        echo Expected:
                        echo %WORKSPACE%\\%PROJECT_DIR%\\pom.xml

                        exit /b 1
                    )

                    echo.
                    echo pom.xml found successfully.
                '''
            }
        }


        // ============================================================
        // 3. RUN BACKEND UNIT / INTEGRATION TESTS
        // ============================================================

        


        // ============================================================
        // 4. BUILD BACKEND
        // ============================================================

        stage('Build Backend') {

            steps {

                echo '=========================================='
                echo 'BUILDING BACKEND'
                echo '=========================================='

                bat '''
                    @echo off

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%MAVEN_HOME%\\bin;%PATH%"

                    cd /d "%WORKSPACE%\\%PROJECT_DIR%"

                    echo.
                    echo Building project...

                    mvn clean package -DskipTests

                    if errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo MAVEN BUILD FAILED
                        echo ==========================================

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo MAVEN BUILD SUCCESSFUL
                    echo ==========================================

                    echo.
                    echo Target directory:

                    dir target
                '''
            }
        }


        // ============================================================
        // 5. VERIFY JAR
        // ============================================================

        stage('Verify Backend JAR') {

            steps {

                echo '=========================================='
                echo 'VERIFYING BACKEND JAR'
                echo '=========================================='

                bat '''
                    @echo off

                    cd /d "%WORKSPACE%\\%PROJECT_DIR%"

                    echo.
                    echo Expected JAR:
                    echo %APP_JAR%

                    if not exist "%APP_JAR%" (

                        echo.
                        echo ==========================================
                        echo ERROR: JAR NOT FOUND
                        echo ==========================================

                        echo.
                        echo Target directory contents:

                        dir target

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo BACKEND JAR FOUND
                    echo ==========================================

                    dir "%APP_JAR%"
                '''
            }
        }


        // ============================================================
        // 6. VERIFY FRONTEND WAR
        // ============================================================

        stage('Verify Frontend WAR Files') {

            steps {

                echo '=========================================='
                echo 'VERIFYING FRONTEND WAR FILES'
                echo '=========================================='

                bat '''
                    @echo off

                    echo.
                    echo AppzillonServer.war:
                    echo %APPZILLON_SERVER_WAR%

                    if not exist "%APPZILLON_SERVER_WAR%" (

                        echo.
                        echo ==========================================
                        echo ERROR: APPZILLON SERVER WAR NOT FOUND
                        echo ==========================================

                        echo Expected:
                        echo %APPZILLON_SERVER_WAR%

                        exit /b 1
                    )

                    echo.
                    echo Appzillon frontend WAR:
                    echo %FRONTEND_WAR%

                    if not exist "%FRONTEND_WAR%" (

                        echo.
                        echo ==========================================
                        echo ERROR: FRONTEND WAR NOT FOUND
                        echo ==========================================

                        echo Expected:
                        echo %FRONTEND_WAR%

                        exit /b 1
                    )

                    echo.
                    echo ==========================================
                    echo FRONTEND WAR FILES FOUND
                    echo ==========================================

                    dir "%APPZILLON_SERVER_WAR%"
                    dir "%FRONTEND_WAR%"
                '''
            }
        }


        // ============================================================
        // 7. STOP EXISTING BACKEND
        // ============================================================

        stage('Stop Backend') {

            steps {

                echo '=========================================='
                echo 'STOPPING EXISTING BACKEND'
                echo '=========================================='

                bat '''
                    @echo off

                    echo Checking port %BACKEND_PORT%...

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%BACKEND_PORT% ^| findstr LISTENING') do (

                        echo Stopping process PID %%a

                        taskkill /F /PID %%a >nul 2>&1
                    )

                    echo.
                    echo Backend stop completed.

                    ping 127.0.0.1 -n 3 >nul
                '''
            }
        }


        // ============================================================
        // 8. START BACKEND
        // ============================================================

        stage('Start Backend') {

            steps {

                echo '=========================================='
                echo 'STARTING BACKEND'
                echo '=========================================='

                bat '''
                    @echo off

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    cd /d "%WORKSPACE%\\%PROJECT_DIR%"

                    echo.
                    echo Starting:
                    echo java -jar %APP_JAR%

                    powershell -NoProfile -ExecutionPolicy Bypass -Command "$env:JENKINS_NODE_COOKIE = 'dontKillMe'; $env:JENKINS_SERVER_COOKIE = 'dontKillMe'; $java = Join-Path $env:JAVA_HOME 'bin\\java.exe'; $process = Start-Process -FilePath $java -ArgumentList @('-jar','%APP_JAR%') -WorkingDirectory '%WORKSPACE%\\%PROJECT_DIR%' -RedirectStandardOutput '%WORKSPACE%\\%PROJECT_DIR%\\backend.log' -RedirectStandardError '%WORKSPACE%\\%PROJECT_DIR%\\backend-error.log' -WindowStyle Hidden -PassThru; if ($process.HasExited) { exit $process.ExitCode }"

                    if errorlevel 1 (

                        echo Backend process could not be started.

                        exit /b 1
                    )

                    echo.
                    echo Backend startup command executed.

                    echo.
                    echo Waiting for backend...

                    ping 127.0.0.1 -n 8 >nul

                    echo.
                    echo ==========================================
                    echo BACKEND LOG
                    echo ==========================================

                    if exist backend.log (

                        powershell -Command "Get-Content backend.log -Tail 50"

                    ) else (

                        echo backend.log not found
                    )

                    if exist backend-error.log (

                        echo.
                        echo Backend error log:

                        type backend-error.log
                    )
                '''
            }
        }


        // ============================================================
        // 9. BACKEND HEALTH CHECK
        // ============================================================

        stage('Backend Health Check') {

            steps {

                echo '=========================================='
                echo 'BACKEND HEALTH CHECK'
                echo '=========================================='

                bat '''
                    @echo off

                    set RETRIES=20

                    :CHECK_BACKEND

                    echo.
                    echo Checking backend port %BACKEND_PORT%...
                    echo Attempts remaining: %RETRIES%

                    netstat -ano | findstr :%BACKEND_PORT% | findstr LISTENING >nul

                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo BACKEND IS RUNNING
                        echo ==========================================

                        echo Backend:
                        echo %BACKEND_URL%

                        exit /b 0
                    )

                    set /a RETRIES-=1

                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo BACKEND FAILED TO START
                        echo ==========================================

                        echo.
                        echo Backend log:

                        if exist "%WORKSPACE%\\%PROJECT_DIR%\\backend.log" (

                            type "%WORKSPACE%\\%PROJECT_DIR%\\backend.log"

                        ) else (

                            echo backend.log not found
                        )

                        exit /b 1
                    )

                    echo Backend not ready.

                    ping 127.0.0.1 -n 4 >nul

                    goto CHECK_BACKEND
                '''
            }
        }


        // ============================================================
        // 10. STOP TOMCAT
        // ============================================================

        stage('Stop Tomcat') {

            steps {

                echo '=========================================='
                echo 'STOPPING TOMCAT'
                echo '=========================================='

                bat '''
                    @echo off

                    if not exist "%TOMCAT_HOME%\\bin\\shutdown.bat" (

                        echo ERROR:
                        echo Tomcat shutdown.bat not found.

                        echo %TOMCAT_HOME%

                        exit /b 1
                    )

                    call "%TOMCAT_HOME%\\bin\\shutdown.bat"

                    echo.
                    echo Waiting for Tomcat to stop...

                    ping 127.0.0.1 -n 6 >nul

                    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%TOMCAT_PORT% ^| findstr LISTENING') do (

                        echo Killing remaining Tomcat PID %%a

                        taskkill /F /PID %%a >nul 2>&1
                    )

                    ping 127.0.0.1 -n 3 >nul

                    echo.
                    echo Tomcat stopped.
                '''
            }
        }


        // ============================================================
        // 11. DEPLOY FRONTEND WAR
        // ============================================================

        stage('Deploy Frontend WAR Files') {

    steps {

        echo '=========================================='
        echo 'DEPLOYING FRONTEND WAR FILES'
        echo '=========================================='

        bat '''
            @echo off

            if not exist "%TOMCAT_HOME%\\webapps" (
                echo ERROR:
                echo Tomcat webapps directory not found.
                exit /b 1
            )

            echo.
            echo ==========================================
            echo REMOVING OLD APPLICATIONS
            echo ==========================================

            rmdir /S /Q "%TOMCAT_HOME%\\webapps\\%APPZILLON_CONTEXT%" 2>nul
            del /F /Q "%TOMCAT_HOME%\\webapps\\%APPZILLON_CONTEXT%.war" 2>nul

            rmdir /S /Q "%TOMCAT_HOME%\\webapps\\%FRONTEND_CONTEXT%" 2>nul
            del /F /Q "%TOMCAT_HOME%\\webapps\\%FRONTEND_CONTEXT%.war" 2>nul

            echo.
            echo ==========================================
            echo COPYING APPZILLON SERVER WAR
            echo ==========================================

            copy /Y "%APPZILLON_SERVER_WAR%" "%TOMCAT_HOME%\\webapps\\%APPZILLON_CONTEXT%.war"

            if errorlevel 1 (
                echo APPZILLON SERVER WAR COPY FAILED
                exit /b 1
            )

            echo.
            echo ==========================================
            echo COPYING FRONTEND WAR
            echo ==========================================

            copy /Y "%FRONTEND_WAR%" "%TOMCAT_HOME%\\webapps\\%FRONTEND_CONTEXT%.war"

            if errorlevel 1 (
                echo FRONTEND WAR COPY FAILED
                exit /b 1
            )

            echo.
            echo ==========================================
            echo VERIFYING WAR FILES
            echo ==========================================

            dir "%TOMCAT_HOME%\\webapps\\%APPZILLON_CONTEXT%.war"
            dir "%TOMCAT_HOME%\\webapps\\%FRONTEND_CONTEXT%.war"

            echo.
            echo WAR files copied successfully.
        '''
    }
}


        // ============================================================
        // 12. START TOMCAT
        // ============================================================

        stage('Start Tomcat') {

            steps {

                echo '=========================================='
                echo 'STARTING TOMCAT'
                echo '=========================================='

                bat '''
                    @echo off

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%PATH%"

                    set "CATALINA_HOME=%TOMCAT_HOME%"
                    set "JENKINS_NODE_COOKIE=dontKillMe"

                    echo JAVA_HOME:
                    echo %JAVA_HOME%

                    echo.
                    echo CATALINA_HOME:
                    echo %CATALINA_HOME%

                    echo.
                    echo Starting Tomcat...

                    call "%TOMCAT_HOME%\\bin\\catalina.bat" start

                    echo.
                    echo Tomcat start command executed.

                    echo.
                    echo Waiting for Tomcat...

                    ping 127.0.0.1 -n 15 >nul


                    echo.
                    echo ==========================================
                    echo TOMCAT PORT CHECK
                    echo ==========================================

                    netstat -ano | findstr :%TOMCAT_PORT% | findstr LISTENING

                    if errorlevel 1 (

                        echo.
                        echo ERROR:
                        echo Tomcat is not listening on port %TOMCAT_PORT%.

                        exit /b 1
                    )

                    echo.
                    echo Tomcat is running successfully.
                '''
            }
        }


        // ============================================================
        // 13. FRONTEND HEALTH CHECK
        // ============================================================

        stage('Frontend Health Check') {

            steps {

                echo '=========================================='
                echo 'FRONTEND HEALTH CHECK'
                echo '=========================================='

                bat '''
                    @echo off

                    echo Frontend URL:
                    echo %FRONTEND_URL%

                    set RETRIES=30

                    :CHECK_FRONTEND

                    echo.
                    echo Checking frontend...
                    echo Attempts remaining: %RETRIES%

                    curl -s -o nul -w "%%{http_code}" "%FRONTEND_URL%" | findstr "200 302"

                    if not errorlevel 1 (

                        echo.
                        echo ==========================================
                        echo FRONTEND IS RUNNING
                        echo ==========================================

                        echo URL:
                        echo %FRONTEND_URL%

                        exit /b 0
                    )

                    set /a RETRIES-=1

                    if %RETRIES% LEQ 0 (

                        echo.
                        echo ==========================================
                        echo FRONTEND HEALTH CHECK FAILED
                        echo ==========================================

                        echo.
                        echo Tomcat port:

                        netstat -ano | findstr :%TOMCAT_PORT%

                        echo.
                        echo Tomcat webapps:

                        dir "%TOMCAT_HOME%\\webapps"

                        echo.
                        echo Tomcat logs:

                        dir "%TOMCAT_HOME%\\logs"

                        exit /b 1
                    )

                    echo Frontend not ready.

                    ping 127.0.0.1 -n 4 >nul

                    goto CHECK_FRONTEND
                '''
            }
        }


        // ============================================================
        // 14. PLAYWRIGHT TESTS
        // ============================================================

        stage('Playwright Tests') {

            steps {

                echo '=========================================='
                echo 'RUNNING PLAYWRIGHT TESTS'
                echo '=========================================='

                bat '''
                    @echo off

                    if not exist "%PLAYWRIGHT_DIR%\\pom.xml" (

                        echo.
                        echo ==========================================
                        echo ERROR: PLAYWRIGHT PROJECT NOT FOUND
                        echo ==========================================

                        echo Expected:
                        echo %PLAYWRIGHT_DIR%\\pom.xml

                        exit /b 1
                    )


                    cd /d "%PLAYWRIGHT_DIR%"

                    set "JAVA_HOME=%JAVA_HOME%"
                    set "PATH=%JAVA_HOME%\\bin;%MAVEN_HOME%\\bin;%PATH%"


                    echo.
                    echo Playwright project:
                    echo %PLAYWRIGHT_DIR%

                    echo.
                    echo Frontend:
                    echo %FRONTEND_URL%

                    echo.
                    echo Running Playwright tests...

                    mvn test

                    set PW_EXIT=%errorlevel%


                    if %PW_EXIT% NEQ 0 (

                        echo.
                        echo ==========================================
                        echo PLAYWRIGHT TESTS FAILED
                        echo ==========================================

                        exit /b %PW_EXIT%
                    )


                    echo.
                    echo ==========================================
                    echo PLAYWRIGHT TESTS PASSED
                    echo ==========================================
                '''
            }

            post {

                always {

                    junit allowEmptyResults: true,
                          testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

    }


    // ================================================================
    // POST ACTIONS
    // ================================================================

    post {

        success {

            echo '=========================================='
            echo 'PIPELINE SUCCESSFUL'
            echo '=========================================='

            echo "Backend: ${BACKEND_URL}"
            echo "Frontend: ${FRONTEND_URL}"

            echo '=========================================='
        }


        failure {

            echo '=========================================='
            echo 'PIPELINE FAILED'
            echo '=========================================='

            echo "Backend log: ${WORKSPACE}\\${PROJECT_DIR}\\backend.log"
            echo "Tomcat logs: ${TOMCAT_HOME}\\logs\\"

            echo '=========================================='
        }
    }
}