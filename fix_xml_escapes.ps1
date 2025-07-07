# 获取所有XML文件
$xmlFiles = Get-ChildItem -Path "storytelling-dao/src/main/resources/mapper" -Filter "*.xml" -Recurse

foreach ($file in $xmlFiles) {
    Write-Host "Processing $($file.FullName)"
    
    # 读取文件内容
    $content = Get-Content $file.FullName -Raw
    
    # 替换SQL语句中的比较运算符
    $content = $content -replace '(?<=\s)>(?=\s|\d)', '&gt;'
    $content = $content -replace '(?<=\s)<(?=\s|\d)', '&lt;'
    $content = $content -replace '(?<=\d)>(?=\s|\d|$)', '&gt;'
    $content = $content -replace '(?<=\d)<(?=\s|\d|$)', '&lt;'
    
    # 保存修改后的内容（使用UTF8NoBOM避免中文乱码）
    $content | Set-Content $file.FullName -Force -Encoding UTF8NoBOM
    
    Write-Host "Completed processing $($file.FullName)"
}

Write-Host "All XML files have been processed."