--[[
    在后续版本中会把os,pcall和xpcall模块给屏蔽，所以在写代码时，请不要使用捕获异常的函数
--]]
function pcall(x)   --屏蔽pcall
    x()
end
function try()
    while(true)     --死循环会卡死服务器
    do
        snowball()
    end
end
while(true)
do
x = pcall(try)
end


