#!/bin/bash

is_weekend() {
    day=$(date +%u)
    [ "$day" -eq 6 ] || [ "$day" -eq 7 ]
}

if is_weekend; then
    # 如果是周末，使用当前时间
    git add .
    git commit -m "$1"
    git push
else
    # 如果不是周末，设置为最近的周末时间
    day=$(date +%u)
    if [ "$day" -le 5 ]; then
        # 周一到周五，设置为即将到来的周六
        days_until_saturday=$((6 - day))
        weekend_date=$(date -d "$days_until_saturday days" "+%Y-%m-%d 12:00:00")
    else
        # 周日，使用昨天（周六）
        weekend_date=$(date -d "yesterday" "+%Y-%m-%d 12:00:00")
    fi
    
    export GIT_AUTHOR_DATE="$weekend_date"
    export GIT_COMMITTER_DATE="$weekend_date"
    
    git add .
    git commit -m "$1"
    git push
    
    unset GIT_AUTHOR_DATE
    unset GIT_COMMITTER_DATE
fi
