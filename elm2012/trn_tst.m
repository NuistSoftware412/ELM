function trn_tst( trn_data,tst_data,outfile )
%TRN_TST Summary of this function goes here
%   Detailed explanation goes here
% Training Time, Testing Time, Testing Accuracy, Testing Accuracy Var
data_size=[500,750,1000,1250,1500,1750,2000,2250,2500,2750,3000];
num_of_trial = 10;
trnd=load(trn_data);
tstd=load(tst_data);
training_time=zeros(num_of_trial,1);
testing_time=zeros(num_of_trial,1);
testing_accuracy=zeros(num_of_trial,1);
fid=fopen(outfile,'w');
for i=1:length(data_size)
    
    trnd1=trnd(1:data_size(i),:);
    tstd1=tstd(1:data_size(i),:);
%    fprintf(fid,'Data Size: %d\n',data_size(i));
    %ELM
    save 'tmp_trn' trnd1 -ascii;
    save 'tmp_tst' tstd1 -ascii;
    for j=1:num_of_trial
        [TrainingTime, TestingTime, TrainingAcc, TestingAcc] = elm('tmp_trn', 'tmp_tst', 1, 100,'sin');
        training_time(j)=TrainingTime;
        testing_time(j)=TestingTime;
        testing_accuracy(j)=TestingAcc;
    end
    fprintf(fid,'%d &%f &%f &%f',data_size(i),mean(training_time),mean(testing_time),mean(testing_accuracy));
   
    %SVM
    P=trnd1(:,2:4);
    T=trnd1(:,1);
    p=tstd1(:,2:4);
    t=tstd1(:,1);
    for j=1:num_of_trial
        tic;
            model=svmtrain(T,P,'-c 1 -g 0.1 -b 1');
        TrainingTime=toc;
        tic;
            [label,TestingAcc,v]=svmpredict(t,p,model);
        TestingTime=toc;
        training_time(j)=TrainingTime;
        testing_time(j)=TestingTime;
        testing_accuracy(j)=TestingAcc(1)/100;
    end
    fprintf(fid,' &%f &%f &%f',mean(training_time),mean(testing_time),mean(testing_accuracy));
    %BP
    P=P';
    T=T';
    p=p';
    t=t';
    for j=1:num_of_trial
        tic;
            net=newff(P,T,6);
            [net,tr] = train(net,P,T);
        TrainingTime=toc;
        tic;
            s=sim(net,p);
        TestingTime=toc;
        s=floor(s+0.5);
        tmp = t-s;
        count = 0;
        for ii=1:data_size(i)
            if tmp(ii) == 0,
                count=count+1;
            end
        end
        training_time(j)=TrainingTime;
        testing_time(j)=TestingTime;
        testing_accuracy(j)=count/data_size(i);
    end    
    fprintf(fid,' &%f &%f &%f \\\\ \\hline\n',mean(training_time),mean(testing_time),mean(testing_accuracy));
    
    
    
%    fprintf(fid,'--------------------end-----------------------\n');
    
end
fclose(fid);
end

