#!/bin/bash

PID_FILE="pids.out"

echo "=================================================="
echo "Stopping all Spring Boot microservices..."
echo "=================================================="

# Check if the tracking file exists
if [ ! -f "$PID_FILE" ]; then
  echo "Error: '$PID_FILE' not found. No running services tracked."
  exit 1
fi

# Read PIDs into an array
PIDS=()
while IFS= read -r pid; do
  if [ ! -z "$pid" ]; then
    PIDS+=("$pid")
  fi
done < "$PID_FILE"

# Iterate in reverse order to stop applications gracefully
for (( idx=${#PIDS[@]}-1 ; idx>=0 ; idx-- )); do
  PID=${PIDS[idx]}
  
  # Check if the process is actively running
  if kill -0 "$PID" 2>/dev/null; then
    echo "Stopping service process with PID: $PID..."
    kill "$PID"
    
    # Wait briefly to confirm closure
    sleep 1
  else
    echo "Process ID $PID is already dead or not found."
  fi
done

# Clean up the tracking file after completion
rm -f "$PID_FILE"
echo "=================================================="
echo "🎉 All recorded processes have been stopped."
echo "=================================================="