for file in ./../archive/*; do
  if [ "${file##*.}" != "md" ]; then
    rm "$file"
  fi
done
