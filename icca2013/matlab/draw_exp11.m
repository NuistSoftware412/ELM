function draw_exp11(data1,data2,data3)
num = [1,5,10,15,20,25,30,35,40,45];
result1 = num;
result2 = num;
result3 = num;
for i=1:size(data1,1)
    result1(i) = std(data1(i,:));
end
for i=1:size(data2,1)
    result2(i) = std(data2(i,:));
end
for i=1:size(data3,1)
    result3(i) = std(data3(i,:));
end
plot(num,result3,'k-+',num,result1,'r-o',num,result2,'b-*');
legend('5000 TS','15000 TS','25000 TS');
xlabel('Model Number');
ylabel('Standard Deviation');
title('Standard Deviation of Testing Accuracy with Increasing Model Number');
end