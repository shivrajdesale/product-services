bash#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e


# Switch to JDK 17
echo "--------------------------------------------------"
echo "Configuring environment to use JDK 17"
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo "Active Java Version:"
java -version
echo "--------------------------------------------------"


# List of services
SERVICES=(
  "config-server"
  "eureka-server"
  "gateway"
  "auth-service"
  "user-service"
  "job-service"
  "notification-service"
  "file-storage"
)

# Execution logs 
if [ -d "./logs" ]; then
  echo "Clearing previous log files..."
  rm -f ./logs/*.log
else
  mkdir -p ./logs
fi

# Reset the PIDs tracking file
rm -f pids.out
touch pids.out


# Iterate through directories
for SERVICE in "${SERVICES[@]}"; do
  if [ -d "$SERVICE" ]; then
  	echo "********************************************"
    echo "Starting service: [$SERVICE]..."
    cd "$SERVICE"
    
    # Run in backgroud
    ./mvnw spring-boot:run > "../logs/${SERVICE}.log" 2>&1 &
    
    # Save the background process ID
    PID=$!
    echo "Service [$SERVICE] launched with PID: $PID"
	echo "$PID" >> "../pids.out"
    
    # Return to the root directory
    cd ..
    
    # Wait for 5 seconds
    echo "Waiting 10 seconds for initialization..."
    sleep 10
	cat ./logs/${SERVICE}.log
    echo "--------------------------------------------------"
  else
    echo " Warning: Directory '$SERVICE' not found. Skipping..."
    echo "--------------------------------------------------"
  fi
done

echo "All microservices started !"
echo "=================================================="