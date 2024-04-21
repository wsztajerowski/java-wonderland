#!/bin/zsh

sha=$(hexdump -n 4 -v -e '/1 "%02X"' /dev/urandom)

AWS_PROFILE=localstack \
AWS_ENDPOINT=https://s3.localhost.localstack.cloud:4566 \
exec java -jar ./test-wrapper/target/test-wrapper.jar jmh-with-async \
--mongo-connection-string mongodb://localhost:27017/local_test \
--commit-sha="$sha" \
--benchmark-path=./jmh-benchmarks/target/jmh-benchmarks.jar \
--machine-readable-output ./test-wrapper/target/jmh-results.json \
--process-output ./test-wrapper/target/jmh-output.txt \
 -wi 1 -f 1 -i 2 \
"$@"