p=load('svm_tst');
p=p';
s=sim(net,p);
s=floor(s+0.5);
t=load('svm_tst_group');
t=t';
tmp = t-s;
count = 0;
for i=1:3000
    if tmp(i) == 0,
        count=count+1;
    end
end
fprintf('%d  %f\n',count, count/3000);