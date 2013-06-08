function elm_mmtrain()
result = ones(7,5)
for i=1:5
     [trt,tst,tra,tea] = elm('T0/t0_norm_1.data','T2/t2_norm.data',1,30,'sig');
     result(1,i)=tea;
end
for i=1:5
     [trt,tst,tra,tea] = elm('T0/t0_norm_2.data','T2/t2_norm.data',1,30,'sig');
     result(2,i)=tea;
end

for i=1:5
     [trt,tst,tra,tea] = elm('T0/t0_norm_3.data','T2/t2_norm.data',1,30,'sig');
     result(3,i)=tea;
end
for i=1:5
     [trt,tst,tra,tea] = elm('T0/t0_norm_4.data','T2/t2_norm.data',1,30,'sig');
     result(4,i)=tea;
end

for i=1:5
     [trt,tst,tra,tea] = elm('T0/t0_norm_5.data','T2/t2_norm.data',1,30,'sig');
     result(5,i)=tea;
end

for i=1:5
     [trt,tst,tra,tea] = elm('T0/t0_norm_6.data','T2/t2_norm.data',1,30,'sig');
     result(6,i)=tea;
end

for i=1:5
     [trt,tst,tra,tea] = elm('T0/t0_norm_7.data','T2/t2_norm.data',1,30,'sig');
     result(7,i)=tea;
end
save('exp1_result4_data_elm','result');
end