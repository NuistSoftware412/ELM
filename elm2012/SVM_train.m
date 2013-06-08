T=load('svm_trn_group');
P=load('svm_trn');
tic;
    model=svmtrain(T,P,'-c 1 -g 0.1 -b 1');
t=toc;
fprintf('time: %f\n',t);

t=load('svm_tst_group');
p=load('svm_tst');
[label,accuracy,v]=svmpredict(t,p,model);
fprintf('accuracy: %f\n',accuracy(1));