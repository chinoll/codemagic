--[[
    在后续版本中会把os,pcall和xpcall模块给屏蔽，所以在写代码时，请不要使用捕获异常的函数
    错误的示例
--]]
function pcall(x)   --屏蔽pcall
    x()
end
function try()
    while(true)     --这段代码会报错，不会执行
    do
        snowball()
    end
end
while(true)
do
x = pcall(try)
end


