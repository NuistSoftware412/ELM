function draw(data)
num = [1,5,10,15,20,25,30,35,40,45];
result = num;
for i=1:size(data,1)
    result(i) = mean(data(i,:));
end
plot(num,result);
end