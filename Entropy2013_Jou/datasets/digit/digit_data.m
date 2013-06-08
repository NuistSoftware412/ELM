function [P,T,TV] = digit_data
    load train.data
    load test.data
    
    
    dataset(1:size(train,1),:)=train;
    dataset(size(train,1)+1:size(train,1)+size(test,1),:)=test;

    rand_sequence=randperm(size(dataset,1));
    temp_dataset=dataset;
    
    dataset=temp_dataset(rand_sequence, :);

    for i=2:size(dataset,2)-1
        dataset(:,i)=(dataset(:,i)-min(dataset(:,i)))/(max(dataset(:,i))-min(dataset(:,i)))*2-1;
    end             
    
    P1=dataset(1:size(train,1),1:size(dataset,2)-1);
    T1=dataset(1:size(train,1),size(dataset,2));

%    P=P1';
%    T=T1';
    
    %Obtain Random Validation Matrix
    
    X=dataset(size(train,1)+1:size(dataset,1),1:size(dataset,2)-1);
    Y=dataset(size(train,1)+1:size(dataset,1),size(dataset,2));
    
    
%    TV.P=X';
%    TV.T=Y';
    
    fid = fopen('digit_train','w');
    for i=1:size(P1,1)
        fprintf(fid,'%2.8f ',T1(i,1));
        for j=2:39
%            fprintf(fid,' %d:%2.8f',j, P1(i,j));    %   for SVM
            fprintf(fid,' %2.8f', P1(i,j));    %   for ELM
        end
         for j=41:size(P1,2)
%            fprintf(fid,' %d:%2.8f',j, P1(i,j));    %   for SVM
            fprintf(fid,' %2.8f', P1(i,j));    %   for ELM
        end
            fprintf(fid,'\n');
        end
    fclose(fid);

    fid = fopen('digit_test','w');    
    for i=1:size(X,1)
        fprintf(fid,'%2.8f ',Y(i,1));
        for j=2:39
%            fprintf(fid,' %d:%2.8f',j, X(i,j));     %   for SVM
            fprintf(fid,' %2.8f', X(i,j));     %   for ELM
        end
        for j=41:size(X,2)
%            fprintf(fid,' %d:%2.8f',j, X(i,j));     %   for SVM
            fprintf(fid,' %2.8f', X(i,j));     %   for ELM
        end
            fprintf(fid,'\n');
        end
    fclose(fid);