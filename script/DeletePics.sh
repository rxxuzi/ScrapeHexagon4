
dir="./../output/pics"

if [ -d "$dir" ]; then
  echo "Deleting all files in $dir"
  rm -rf "$dir"/*
else
  echo "$dir does not exist"
fi


dir="./../output/scala"

if [ -d "$dir" ]; then
  echo "Deleting all files in $dir"
  rm -rf "$dir"/*
else
  echo "$dir does not exist"
fi

dir="./../output/json"

if [ -d "$dir" ]; then
  echo "Deleting all files in $dir"
  # shellcheck disable=SC2115
  rm -rf "$dir"/*
else
  echo "$dir does not exist"
fi

# read y/n
read -p "Do you want to delete the zip files? (y/n)" -n 1 -r

if [[ $REPLY =~ ^[Yy]$ ]]
then
  echo "Deleting zip files"
  dir="./../archive"

  if [ -d "$dir" ]; then
    # shellcheck disable=SC2115
    for file in ./../archive/*; do
      if [ "${file##*.}" != "md" ]; then
        rm "$file"
      fi
    done
  else
    echo "$dir does not exist"
  fi
else
  echo "Skipping zip files"
fi