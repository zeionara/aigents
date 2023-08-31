#!/bin/bash

result=""

root='javafx-sdk-20.0.2/src'

for file in ./javafx-sdk-20.0.2/src/*; do
    result="$result:./../../../$root/$(basename $file)"
done

echo $result
