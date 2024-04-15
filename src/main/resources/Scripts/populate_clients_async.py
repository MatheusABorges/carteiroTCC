import requests
import concurrent.futures

# List of JSON payloads with port counter starting from 1
payloads = [
    {"ip": "10.0.0.1", "port": i + 1, "textFilter": "urgent", "ratio": 0.5}
    for i in range(1000)
]

# Function to send POST request
def send_post_request(payload):
    url = 'http://localhost:8080/subscription/subscribe'
    headers = {'Content-Type': 'application/json'}
    response = requests.post(url, json=payload, headers=headers)
    print(f'Response for {payload}: {response.status_code}')

# Send POST requests in parallel
with concurrent.futures.ThreadPoolExecutor(max_workers=30) as executor:
    executor.map(send_post_request, payloads)