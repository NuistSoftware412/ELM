svm_trn=load('svm_trn');
P=svm_trn';
svm_trn_group=load('svm_trn_group');
T=svm_trn_group';
tic;
net=newff(P,T,6);
[net,tr] = train(net,P,T);
t=toc;
fprintf('time: %f\n',t);
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
fprintf('Accuracy: %f\n',count/3000);