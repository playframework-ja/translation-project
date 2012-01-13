total_size=$(du --summarize *.textile --total | tail -n 1 | cut -f 1)
translated_files=$(grep -L "Esta página todavía no ha sido traducida al castellano" *.textile)
translated_size=$(echo $translated_files | tr '\n ' '\0' | xargs -0 du --summarize --total | tail -n 1 | cut -f 1)
translated_percent=$(($translated_size*100/$total_size))
echo "translated size: ${translated_size}kb/${total_size}kb ${translated_percent}% \
(pending $(($total_size-$translated_size))kb $((100-$translated_percent))%)"

total_count=$(ls *.textile | wc -l)
translated_count=$(echo $translated_files | tr ' ' '\n' | wc -l)
translated_percent=$(($translated_count*100/$total_count))
echo "translated files: ${translated_count}/${total_count} $(($translated_count*100/$total_count))% \
(pending $(($total_count-$translated_count)) $((100-$translated_percent))%)"
