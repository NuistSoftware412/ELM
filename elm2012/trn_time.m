function [x,y]=trn_time( trn_data,min_size,max_size )
%TRN_TIME Summary of this function goes here
%   Detailed explanation goes here
% To compare the training time with incremental training data size 
% SVM, BP, ELM

step = 50;
num_of_trial = 10;
train_time=zeros(num_of_trial,1);
len = ceil((max_size-min_size+1)/step);
x=zeros(1,len);
y=zeros(3,len);
d = load(trn_data);
l=1;
for i=min_size:step:max_size
    x(1,l)=i;
  
%for ELM
    tmp = d(1:i,:);
    save 'tmp' tmp -ascii;
    for j=1:num_of_trial
        [TrainingTime, TrainingAccuracy] = elm_train('tmp', 1, 100, 'sin');
        train_time(j,1)=TrainingTime;
    end
    y(1,l)=mean(train_time);
   
%for SVM
    svm_trn = d(1:i,2:4);
    svm_trn_group = d(1:i,1);
    for j=1:num_of_trial
        tic;
            svmtrain(svm_trn_group, svm_trn, '-c 1 -g 0.1 -b 1');
        t=toc;
        train_time(j,1)=t;
    end
    y(2,l)=mean(train_time);
    
    
%for BP
    P=d(1:i,2:4)';
    T=d(1:i,1)';
    for j=1:num_of_trial
        tic;
            net=newff(P,T,6);
            net = train(net,P,T);
        t2=toc;
        train_time(j,1)=t2;
    end
    y(3,l)=mean(train_time);
    
    l=l+1;
end

plot(x(1,:),y(1,:),'r');
hold on;
plot(x(1,:),y(2,:),'b');
hold on;
plot(x(1,:),y(3,:),'g');


legend('Red: ELM','Blue: SVM','Green: BP');

end

