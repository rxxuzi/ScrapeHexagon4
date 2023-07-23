#include <iostream>
#include <curl/curl.h>

int main() {
    CURL *curl;
    CURLcode res;
    std::string readBuffer;

    curl = curl_easy_init();
    if (curl) {
        curl_easy_setopt(curl, CURLOPT_URL, "http://example.com");
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, [](char *data, size_t size, size_t nmemb, std::string *writerData) -> size_t {
            if (writerData == nullptr) {
                return 0;
            }
            writerData->append(data, size * nmemb);
            return size * nmemb;
        });
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &readBuffer);
        res = curl_easy_perform(curl);
        curl_easy_cleanup(curl);

        if (res == CURLE_OK) {
            std::cout << readBuffer << std::endl;
        }
    }

    return 0;
}
